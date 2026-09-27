package com.obsystem.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.ProdutoDTO;
import com.obsystem.entities.Produto;
import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.ProdutoStatus;
import com.obsystem.entities.enums.ProdutoTipo;
import com.obsystem.repositories.ProdutoRepository;
import com.obsystem.services.exceptions.BusinessException;
import com.obsystem.services.exceptions.ResourceNotFoundException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Transactional(readOnly = true)
    public List<ProdutoDTO> findAll(String search, ProdutoStatus status) {
        List<Produto> list;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasStatus = status != null;

        if (hasSearch && hasStatus) {
            String s = search.trim();
            list = repository.findByNomeProdutoContainingIgnoreCaseOrCodigoProdutoContainingIgnoreCase(s, s)
                    .stream()
                    .filter(p -> p.getStatusProduto() == status)
                    .collect(Collectors.toList());
        } else if (hasSearch) {
            String s = search.trim();
            list = repository.findByNomeProdutoContainingIgnoreCaseOrCodigoProdutoContainingIgnoreCase(s, s);
        } else if (hasStatus) {
            list = repository.findByStatusProduto(status);
        } else {
            list = repository.findAll();
        }

        return list.stream().map(ProdutoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoDTO findById(Integer id) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + id));
        return new ProdutoDTO(entity);
    }

    @Transactional
    public ProdutoDTO create(ProdutoDTO dto) {
        validarRegrasDeNegocio(dto, null);

        Produto entity = new Produto();
        copiarDtoParaEntidade(dto, entity);

        // Se status não foi informado, inicia ATIVO
        if (entity.getStatusProduto() == null) {
            entity.setStatusProduto(ProdutoStatus.ATIVO);
        }

        // Calcula estoque disponível
        BigDecimal atual = entity.getEstoqueAtual() != null ? entity.getEstoqueAtual() : BigDecimal.ZERO;
        BigDecimal reservado = entity.getEstoqueReservado() != null ? entity.getEstoqueReservado() : BigDecimal.ZERO;
        entity.setEstoqueDisponivel(atual.subtract(reservado));

        entity = repository.save(entity);
        return new ProdutoDTO(entity);
    }

    @Transactional
    public ProdutoDTO update(Integer id, ProdutoDTO dto) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + id));

        validarRegrasDeNegocio(dto, id);
        copiarDtoParaEntidade(dto, entity);

        // Recalcula estoque disponível
        BigDecimal atual = entity.getEstoqueAtual() != null ? entity.getEstoqueAtual() : BigDecimal.ZERO;
        BigDecimal reservado = entity.getEstoqueReservado() != null ? entity.getEstoqueReservado() : BigDecimal.ZERO;
        entity.setEstoqueDisponivel(atual.subtract(reservado));

        entity = repository.save(entity);
        return new ProdutoDTO(entity);
    }

    @Transactional
    public void inativar(Integer id) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + id));
        entity.setStatusProduto(ProdutoStatus.INATIVO);
        repository.save(entity);
    }

    @Transactional
    public void ativar(Integer id) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + id));
        entity.setStatusProduto(ProdutoStatus.ATIVO);
        repository.save(entity);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado para o ID: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("Não é possível excluir o produto pois ele possui histórico de movimentações. Considere inativá-lo.");
        }
    }

    /**
     * Validações profundas de negócio do produto
     */
    private void validarRegrasDeNegocio(ProdutoDTO dto, Integer idAtual) {
        // 1. Validação do Nome
        if (dto.getNomeProduto() == null || dto.getNomeProduto().trim().isEmpty()) {
            throw new BusinessException("O nome do produto é obrigatório.");
        }

        // 2. Validação e Unicidade do Código (SKU)
        if (dto.getCodigoProduto() == null || dto.getCodigoProduto().trim().isEmpty()) {
            throw new BusinessException("O código/SKU do produto é obrigatório.");
        }

        String codigoLimpo = dto.getCodigoProduto().trim();
        if (idAtual == null) {
            // Novo cadastro
            if (repository.existsByCodigoProduto(codigoLimpo)) {
                throw new BusinessException("Já existe um produto cadastrado com o código: " + codigoLimpo);
            }
        } else {
            // Edição existente
            if (repository.existsByCodigoProdutoAndIdNot(codigoLimpo, idAtual)) {
                throw new BusinessException("Já existe outro produto cadastrado com o código: " + codigoLimpo);
            }
        }

        // 3. Validação de Preços
        if (dto.getPrecoCusto() != null && dto.getPrecoCusto().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O preço de custo não pode ser negativo.");
        }
        if (dto.getPrecoVenda() != null && dto.getPrecoVenda().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O preço de venda não pode ser negativo.");
        }

        // 4. Validação de Estoque
        BigDecimal min = dto.getEstoqueMinimo() != null ? dto.getEstoqueMinimo() : BigDecimal.ZERO;
        BigDecimal max = dto.getEstoqueMaximo() != null ? dto.getEstoqueMaximo() : BigDecimal.ZERO;

        if (min.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O estoque mínimo não pode ser negativo.");
        }
        if (max.compareTo(BigDecimal.ZERO) > 0 && min.compareTo(max) > 0) {
            throw new BusinessException("O estoque mínimo não pode ser maior que o estoque máximo.");
        }
    }

    private void copiarDtoParaEntidade(ProdutoDTO dto, Produto entity) {
        entity.setNomeProduto(dto.getNomeProduto().trim());
        entity.setCodigoProduto(dto.getCodigoProduto().trim());
        entity.setPrecoCusto(dto.getPrecoCusto() != null ? dto.getPrecoCusto() : BigDecimal.ZERO);
        entity.setPrecoVenda(dto.getPrecoVenda() != null ? dto.getPrecoVenda() : BigDecimal.ZERO);
        entity.setEstoqueMinimo(dto.getEstoqueMinimo() != null ? dto.getEstoqueMinimo() : BigDecimal.ZERO);
        entity.setEstoqueMaximo(dto.getEstoqueMaximo() != null ? dto.getEstoqueMaximo() : BigDecimal.ZERO);
        entity.setEstoqueReservado(dto.getEstoqueReservado() != null ? dto.getEstoqueReservado() : BigDecimal.ZERO);
        entity.setEstoqueAtual(dto.getEstoqueAtual() != null ? dto.getEstoqueAtual() : BigDecimal.ZERO);
        entity.setTipoProduto(dto.getTipoProduto() != null ? dto.getTipoProduto() : ProdutoTipo.PRODUTO);
        entity.setUnidadeMedida(dto.getUnidadeMedida());
        entity.setCategoria(dto.getCategoria());
        entity.setSubcategoria(dto.getSubcategoria());
        entity.setModelo(dto.getModelo());
        entity.setMilimetro(dto.getMilimetro());
        entity.setMedida(dto.getMedida());
        entity.setMarca(dto.getMarca());
        entity.setControlaEstoque(dto.getControlaEstoque() != null ? dto.getControlaEstoque() : ProdutoEstoque.SIM);
        entity.setFracionar(dto.getFracionar() != null ? dto.getFracionar() : 'N');
        if (dto.getStatusProduto() != null) {
            entity.setStatusProduto(dto.getStatusProduto());
        }
    }
}

package com.obsystem.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.CompraDTO;
import com.obsystem.dto.ItemCompraDTO;
import com.obsystem.entities.Compra;
import com.obsystem.entities.Fornecedor;
import com.obsystem.entities.ItemCompra;
import com.obsystem.entities.Produto;
import com.obsystem.entities.Titulo;
import com.obsystem.entities.enums.CompraStatus;
import com.obsystem.entities.enums.FornecedorStatus;
import com.obsystem.entities.enums.ItemStatus;
import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;
import com.obsystem.repositories.CompraRepository;
import com.obsystem.repositories.FornecedorRepository;
import com.obsystem.repositories.ProdutoRepository;
import com.obsystem.repositories.TituloRepository;
import com.obsystem.services.exceptions.BusinessException;
import com.obsystem.services.exceptions.ResourceNotFoundException;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TituloRepository tituloRepository;

    @Transactional(readOnly = true)
    public List<CompraDTO> findAll(String search, CompraStatus status) {
        List<Compra> list;

        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasStatus = status != null;

        if (hasSearch || hasStatus) {
            String s = hasSearch ? search.trim() : null;
            list = compraRepository.searchCompras(s, status);
        } else {
            list = compraRepository.findAllOrderByEmissaoDesc();
        }

        return list.stream().map(CompraDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompraDTO findById(Integer id) {
        Compra entity = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada para o ID: " + id));
        return new CompraDTO(entity);
    }

    @Transactional
    public CompraDTO create(CompraDTO dto) {
        if (dto.getIdFornecedor() == null) {
            throw new BusinessException("O fornecedor é obrigatório para registrar a compra.");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(dto.getIdFornecedor())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado para o ID: " + dto.getIdFornecedor()));

        if (fornecedor.getStatus() != FornecedorStatus.ATIVO) {
            throw new BusinessException("Não é possível realizar compras com fornecedor inativo ou bloqueado.");
        }

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new BusinessException("A compra/cotação deve conter pelo menos um item.");
        }

        Compra compra = new Compra();
        compra.setFornecedor(fornecedor);
        compra.setEmissao(dto.getEmissao() != null ? dto.getEmissao() : LocalDate.now());
        compra.setPrevisao(dto.getPrevisao() != null ? dto.getPrevisao() : LocalDate.now());
        compra.setStatusCompra(dto.getStatusCompra() != null ? dto.getStatusCompra() : CompraStatus.COTACAO);

        BigDecimal totalCalculado = BigDecimal.ZERO;
        int itemIndex = 1;

        for (ItemCompraDTO itemDto : dto.getItens()) {
            if (itemDto.getIdProduto() == null) {
                throw new BusinessException("Todo item deve referenciar um produto/insumo válido.");
            }

            Produto produto = produtoRepository.findById(itemDto.getIdProduto())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + itemDto.getIdProduto()));

            BigDecimal qtd = itemDto.getQuantidadeItem() != null ? itemDto.getQuantidadeItem() : BigDecimal.ONE;
            if (qtd.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("A quantidade do item " + produto.getNomeProduto() + " deve ser maior que zero.");
            }

            // Valida fracionamento
            if (produto.getFracionar() != null && produto.getFracionar() == 'N') {
                if (qtd.stripTrailingZeros().scale() > 0) {
                    throw new BusinessException("O produto " + produto.getNomeProduto() + " não permite quantidade fracionada.");
                }
            }

            BigDecimal unitario = itemDto.getPrecoUnitario() != null ? itemDto.getPrecoUnitario() : produto.getPrecoCusto();
            BigDecimal desc = itemDto.getValorDesconto() != null ? itemDto.getValorDesconto() : BigDecimal.ZERO;
            BigDecimal desp = itemDto.getValorDespesa() != null ? itemDto.getValorDespesa() : BigDecimal.ZERO;
            BigDecimal subtotal = unitario.multiply(qtd).subtract(desc).add(desp);

            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setCompra(compra);
            itemCompra.setProduto(produto);
            itemCompra.setNumeroItem(itemIndex++);
            itemCompra.setQuantidadeItem(qtd);
            itemCompra.setPrecoUnitario(unitario);
            itemCompra.setValorDesconto(desc);
            itemCompra.setValorDespesa(desp);
            itemCompra.setValorTotal(subtotal);
            itemCompra.setStatusItem(ItemStatus.INCLUSO);

            compra.getItens().add(itemCompra);
            totalCalculado = totalCalculado.add(subtotal);

            // Se criado diretamente como RECEBIMENTO ou PAGAMENTO, executa entrada no estoque
            if (compra.getStatusCompra() == CompraStatus.RECEBIMENTO || compra.getStatusCompra() == CompraStatus.PAGAMENTO) {
                darEntradaEstoque(produto, qtd, unitario);
            }
        }

        compra.setTotalCompra(totalCalculado);
        compra = compraRepository.save(compra);

        // Se status finalizado em PAGAMENTO e solicitou gerar contas a pagar
        if (compra.getStatusCompra() == CompraStatus.PAGAMENTO && Boolean.TRUE.equals(dto.getGerarTituloFinanceiro())) {
            gerarTituloContasPagar(compra);
        }

        return new CompraDTO(compra);
    }

    @Transactional
    public CompraDTO alterarStatus(Integer id, CompraStatus novoStatus) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada para o ID: " + id));

        CompraStatus statusAtual = compra.getStatusCompra();
        if (statusAtual == novoStatus) {
            return new CompraDTO(compra);
        }

        // Se estava em estágio anterior a RECEBIMENTO e agora atinge RECEBIMENTO ou PAGAMENTO: efetiva entrada no estoque
        boolean jaTeveEntrada = (statusAtual == CompraStatus.RECEBIMENTO || statusAtual == CompraStatus.PAGAMENTO);
        if (!jaTeveEntrada && (novoStatus == CompraStatus.RECEBIMENTO || novoStatus == CompraStatus.PAGAMENTO)) {
            for (ItemCompra item : compra.getItens()) {
                Produto produto = item.getProduto();
                darEntradaEstoque(produto, item.getQuantidadeItem(), item.getPrecoUnitario());
            }
        }

        // Se atinge estágio PAGAMENTO, gera Contas a Pagar
        if (novoStatus == CompraStatus.PAGAMENTO) {
            gerarTituloContasPagar(compra);
        }

        compra.setStatusCompra(novoStatus);
        compra = compraRepository.save(compra);
        return new CompraDTO(compra);
    }

    private void darEntradaEstoque(Produto produto, BigDecimal qtd, BigDecimal novoCusto) {
        if (produto.getControlaEstoque() == ProdutoEstoque.SIM) {
            BigDecimal atual = produto.getEstoqueAtual() != null ? produto.getEstoqueAtual() : BigDecimal.ZERO;
            BigDecimal reservado = produto.getEstoqueReservado() != null ? produto.getEstoqueReservado() : BigDecimal.ZERO;

            produto.setEstoqueAtual(atual.add(qtd));
            produto.setEstoqueDisponivel(produto.getEstoqueAtual().subtract(reservado));

            // Atualiza preço de custo com o valor negociado na compra
            if (novoCusto != null && novoCusto.compareTo(BigDecimal.ZERO) > 0) {
                produto.setPrecoCusto(novoCusto);
            }

            produtoRepository.save(produto);
        }
    }

    private void gerarTituloContasPagar(Compra compra) {
        Titulo titulo = new Titulo();
        titulo.setTipo(TipoTitulo.DESPESA);
        titulo.setDescricao("Compra nº " + compra.getId() + " - " + compra.getFornecedor().getPessoa().getNome());
        titulo.setPessoa(compra.getFornecedor().getPessoa());
        titulo.setCompra(compra);
        titulo.setEmissao(compra.getEmissao());
        titulo.setVencimento(compra.getPrevisao() != null ? compra.getPrevisao() : LocalDate.now());
        titulo.setValorTotal(compra.getTotalCompra());
        titulo.setStatus(TituloStatus.PENDENTE);
        tituloRepository.save(titulo);
    }
}

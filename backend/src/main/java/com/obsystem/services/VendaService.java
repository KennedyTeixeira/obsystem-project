package com.obsystem.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.obsystem.dto.ItemVendaDTO;
import com.obsystem.dto.VendaDTO;
import com.obsystem.entities.Cliente;
import com.obsystem.entities.ItemVenda;
import com.obsystem.entities.Produto;
import com.obsystem.entities.Titulo;
import com.obsystem.entities.Venda;
import com.obsystem.entities.enums.ClienteStatus;
import com.obsystem.entities.enums.ItemStatus;
import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;
import com.obsystem.entities.enums.VendaStatus;
import com.obsystem.repositories.ClienteRepository;
import com.obsystem.repositories.ProdutoRepository;
import com.obsystem.repositories.TituloRepository;
import com.obsystem.repositories.VendaRepository;
import com.obsystem.services.exceptions.BusinessException;
import com.obsystem.services.exceptions.ResourceNotFoundException;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TituloRepository tituloRepository;

    @Transactional(readOnly = true)
    public List<VendaDTO> findAll(String search, VendaStatus status) {
        List<Venda> list;

        if (search != null && !search.trim().isEmpty()) {
            list = vendaRepository.searchByClienteNome(search.trim());
        } else if (status != null) {
            list = vendaRepository.findByStatusVenda(status);
        } else {
            list = vendaRepository.findAllOrderByEmissaoDesc();
        }

        return list.stream().map(VendaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VendaDTO findById(Integer id) {
        Venda entity = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada para o ID: " + id));
        return new VendaDTO(entity);
    }

    @Transactional
    public VendaDTO create(VendaDTO dto) {
        if (dto.getIdCliente() == null) {
            throw new BusinessException("O cliente é obrigatório para realizar a venda.");
        }

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + dto.getIdCliente()));

        if (cliente.getStatus() != ClienteStatus.ATIVO) {
            throw new BusinessException("Não é possível realizar venda para um cliente inativo ou bloqueado.");
        }

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new BusinessException("A venda deve conter pelo menos um item.");
        }

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setEmissao(dto.getEmissao() != null ? dto.getEmissao() : LocalDate.now());
        venda.setPrevisao(dto.getPrevisao() != null ? dto.getPrevisao() : LocalDate.now());
        venda.setStatusVenda(dto.getStatusVenda() != null ? dto.getStatusVenda() : VendaStatus.ABERTO);

        BigDecimal totalCalculado = BigDecimal.ZERO;
        int itemIndex = 1;

        for (ItemVendaDTO itemDto : dto.getItens()) {
            if (itemDto.getIdProduto() == null) {
                throw new BusinessException("Todo item deve referenciar um produto válido.");
            }

            Produto produto = produtoRepository.findById(itemDto.getIdProduto())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o ID: " + itemDto.getIdProduto()));

            BigDecimal qtd = itemDto.getQuantidadeItem() != null ? itemDto.getQuantidadeItem() : BigDecimal.ONE;
            if (qtd.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("A quantidade do produto " + produto.getNomeProduto() + " deve ser maior que zero.");
            }

            // Valida fracionamento
            if (produto.getFracionar() != null && produto.getFracionar() == 'N') {
                if (qtd.stripTrailingZeros().scale() > 0) {
                    throw new BusinessException("O produto " + produto.getNomeProduto() + " não permite quantidade fracionada.");
                }
            }

            // Validação e reserva/baixa de estoque
            if (produto.getControlaEstoque() == ProdutoEstoque.SIM && venda.getStatusVenda() != VendaStatus.ORCAMENTO) {
                BigDecimal disponivel = produto.getEstoqueDisponivel() != null ? produto.getEstoqueDisponivel() : BigDecimal.ZERO;
                if (disponivel.compareTo(qtd) < 0) {
                    throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNomeProduto() + 
                            ". Disponível: " + disponivel + " " + produto.getUnidadeMedida());
                }

                if (venda.getStatusVenda() == VendaStatus.FINALIZADO) {
                    // Baixa direta
                    produto.setEstoqueAtual(produto.getEstoqueAtual().subtract(qtd));
                } else {
                    // Reserva
                    produto.setEstoqueReservado(produto.getEstoqueReservado().add(qtd));
                }
                produto.setEstoqueDisponivel(produto.getEstoqueAtual().subtract(produto.getEstoqueReservado()));
                produtoRepository.save(produto);
            }

            BigDecimal unitario = itemDto.getPrecoUnitario() != null ? itemDto.getPrecoUnitario() : produto.getPrecoVenda();
            BigDecimal desc = itemDto.getValorDesconto() != null ? itemDto.getValorDesconto() : BigDecimal.ZERO;
            BigDecimal acresc = itemDto.getValorAcrescimo() != null ? itemDto.getValorAcrescimo() : BigDecimal.ZERO;
            BigDecimal subtotal = unitario.multiply(qtd).subtract(desc).add(acresc);

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(venda);
            itemVenda.setProduto(produto);
            itemVenda.setNumeroItem(itemIndex++);
            itemVenda.setQuantidadeItem(qtd);
            itemVenda.setPrecoUnitario(unitario);
            itemVenda.setValorDesconto(desc);
            itemVenda.setValorAcrescimo(acresc);
            itemVenda.setValorTotal(subtotal);
            itemVenda.setValorCusto(produto.getPrecoCusto());
            itemVenda.setStatusItem(ItemStatus.INCLUSO);

            venda.getItens().add(itemVenda);
            totalCalculado = totalCalculado.add(subtotal);
        }

        venda.setTotalVenda(totalCalculado);
        venda = vendaRepository.save(venda);

        // Se finalizado imediatamente e solicitou gerar título, cria Contas a Receber
        if (venda.getStatusVenda() == VendaStatus.FINALIZADO && Boolean.TRUE.equals(dto.getGerarTituloFinanceiro())) {
            gerarTituloContasReceber(venda);
        }

        return new VendaDTO(venda);
    }

    @Transactional
    public VendaDTO alterarStatus(Integer id, VendaStatus novoStatus) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada para o ID: " + id));

        VendaStatus statusAtual = venda.getStatusVenda();
        if (statusAtual == novoStatus) {
            return new VendaDTO(venda);
        }

        // Transição para FINALIZADO
        if (novoStatus == VendaStatus.FINALIZADO) {
            for (ItemVenda item : venda.getItens()) {
                Produto p = item.getProduto();
                if (p.getControlaEstoque() == ProdutoEstoque.SIM) {
                    BigDecimal qtd = item.getQuantidadeItem();
                    // Se estava reservado, desreserva e subtrai do estoque atual
                    if (statusAtual == VendaStatus.ABERTO || statusAtual == VendaStatus.SEPARACAO) {
                        p.setEstoqueReservado(p.getEstoqueReservado().subtract(qtd));
                    }
                    p.setEstoqueAtual(p.getEstoqueAtual().subtract(qtd));
                    p.setEstoqueDisponivel(p.getEstoqueAtual().subtract(p.getEstoqueReservado()));
                    produtoRepository.save(p);
                }
            }
            gerarTituloContasReceber(venda);
        }

        venda.setStatusVenda(novoStatus);
        venda = vendaRepository.save(venda);
        return new VendaDTO(venda);
    }

    private void gerarTituloContasReceber(Venda venda) {
        Titulo titulo = new Titulo();
        titulo.setTipo(TipoTitulo.RECEITA);
        titulo.setDescricao("Venda nº " + venda.getId() + " - " + venda.getCliente().getPessoa().getNome());
        titulo.setPessoa(venda.getCliente().getPessoa());
        titulo.setVenda(venda);
        titulo.setEmissao(venda.getEmissao());
        titulo.setVencimento(venda.getPrevisao() != null ? venda.getPrevisao() : LocalDate.now());
        titulo.setValorTotal(venda.getTotalVenda());
        titulo.setStatus(TituloStatus.PENDENTE);
        tituloRepository.save(titulo);
    }
}

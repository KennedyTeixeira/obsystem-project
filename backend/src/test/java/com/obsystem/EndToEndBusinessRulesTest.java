package com.obsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.obsystem.dto.ClienteDTO;
import com.obsystem.dto.CompraDTO;
import com.obsystem.dto.FornecedorDTO;
import com.obsystem.dto.ItemCompraDTO;
import com.obsystem.dto.ItemVendaDTO;
import com.obsystem.dto.ProdutoDTO;
import com.obsystem.dto.ResumoFinanceiroDTO;
import com.obsystem.dto.TituloDTO;
import com.obsystem.dto.VendaDTO;
import com.obsystem.entities.enums.ClienteStatus;
import com.obsystem.entities.enums.CompraStatus;
import com.obsystem.entities.enums.FornecedorStatus;
import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.ProdutoStatus;
import com.obsystem.entities.enums.ProdutoTipo;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;
import com.obsystem.entities.enums.VendaStatus;
import com.obsystem.repositories.ProdutoRepository;
import com.obsystem.repositories.TituloRepository;
import com.obsystem.services.ClienteService;
import com.obsystem.services.CompraService;
import com.obsystem.services.FornecedorService;
import com.obsystem.services.ProdutoService;
import com.obsystem.services.TituloService;
import com.obsystem.services.VendaService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndBusinessRulesTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private TituloService tituloService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TituloRepository tituloRepository;

    private static Integer clienteId;
    private static Integer fornecedorId;
    private static Integer produtoId;
    private static Integer compraId;
    private static Integer vendaId;
    private static Integer tituloPagarId;
    private static Integer tituloReceberId;

    private static final String TIMESTAMP = String.valueOf(System.currentTimeMillis()).substring(6);

    @Test
    @Order(1)
    void teste1_ConexaoBancoDeDados() throws Exception {
        assertNotNull(dataSource, "DataSource deve estar configurado no Spring");
        try (var conn = dataSource.getConnection()) {
            assertTrue(conn.isValid(5), "Conexão com PostgreSQL Supabase deve ser válida");
            assertEquals("postgres", conn.getCatalog(), "Catálogo do banco de dados deve ser postgres");
        }
    }

    @Test
    @Order(2)
    void teste2_CriacaoEValidacaoCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Cliente Teste E2E " + TIMESTAMP);
        dto.setTipoPessoa('F');
        dto.setCpf("111" + TIMESTAMP + "00");
        dto.setEmail("cliente." + TIMESTAMP + "@teste.com");
        dto.setStatus(ClienteStatus.ATIVO);
        dto.setLimite(new BigDecimal("5000.00"));

        ClienteDTO criado = clienteService.create(dto);
        assertNotNull(criado.getId(), "Cliente criado deve receber ID gerado");
        assertNotNull(criado.getIdPessoa(), "Pessoa vinculada deve receber ID");
        assertEquals(ClienteStatus.ATIVO, criado.getStatus());

        clienteId = criado.getId();

        // Consulta de validação
        ClienteDTO consultado = clienteService.findById(clienteId);
        assertEquals(dto.getNome(), consultado.getNome());
    }

    @Test
    @Order(3)
    void teste3_CriacaoEValidacaoFornecedor() {
        FornecedorDTO dto = new FornecedorDTO();
        dto.setNome("Fornecedor Vidros E2E " + TIMESTAMP);
        dto.setTipoPessoa('J');
        dto.setCnpj("22" + TIMESTAMP + "000199");
        dto.setFantasia("Vidros E2E");
        dto.setEmail("fornecedor." + TIMESTAMP + "@teste.com");
        dto.setStatus(FornecedorStatus.ATIVO);

        FornecedorDTO criado = fornecedorService.create(dto);
        assertNotNull(criado.getId(), "Fornecedor criado deve receber ID gerado");
        assertNotNull(criado.getIdPessoa(), "Pessoa jurídica vinculada deve receber ID");
        assertEquals(FornecedorStatus.ATIVO, criado.getStatus());

        fornecedorId = criado.getId();

        // Consulta de validação
        FornecedorDTO consultado = fornecedorService.findById(fornecedorId);
        assertEquals(dto.getNome(), consultado.getNome());
    }

    @Test
    @Order(4)
    void teste4_CriacaoProdutoERegrasEstoque() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNomeProduto("Vidro Temperado E2E " + TIMESTAMP);
        dto.setCodigoProduto("SKU-" + TIMESTAMP);
        dto.setPrecoCusto(new BigDecimal("100.00"));
        dto.setPrecoVenda(new BigDecimal("200.00"));
        dto.setEstoqueMinimo(new BigDecimal("5.00"));
        dto.setEstoqueMaximo(new BigDecimal("100.00"));
        dto.setEstoqueAtual(new BigDecimal("10.00"));
        dto.setControlaEstoque(ProdutoEstoque.SIM);
        dto.setTipoProduto(ProdutoTipo.PRODUTO);
        dto.setUnidadeMedida("M2");

        ProdutoDTO criado = produtoService.create(dto);
        assertNotNull(criado.getId());
        assertEquals(0, new BigDecimal("10.00").compareTo(criado.getEstoqueAtual()));
        assertEquals(ProdutoStatus.ATIVO, criado.getStatusProduto());

        produtoId = criado.getId();

        // Validação de margem de lucro (200 - 100) / 100 = 100.00%
        assertEquals(0, new BigDecimal("100.00").compareTo(criado.getMargemLucro()));
    }

    @Test
    @Order(5)
    void teste5_CicloCompraEEntradaEstoqueEContasPagar() {
        assertNotNull(fornecedorId);
        assertNotNull(produtoId);

        CompraDTO compra = new CompraDTO();
        compra.setIdFornecedor(fornecedorId);
        compra.setEmissao(LocalDate.now());
        compra.setPrevisao(LocalDate.now().plusDays(10));
        compra.setStatusCompra(CompraStatus.COTACAO);
        compra.setGerarTituloFinanceiro(true);

        ItemCompraDTO item = new ItemCompraDTO();
        item.setIdProduto(produtoId);
        item.setQuantidadeItem(new BigDecimal("20.00"));
        item.setPrecoUnitario(new BigDecimal("80.00")); // negociado a R$ 80
        item.setValorDesconto(new BigDecimal("10.00"));
        item.setValorDespesa(new BigDecimal("30.00")); // frete

        List<ItemCompraDTO> itens = new ArrayList<>();
        itens.add(item);
        compra.setItens(itens);

        // 1. Criação em COTAÇÃO: total = (20 * 80) - 10 + 30 = 1620.00
        CompraDTO criada = compraService.create(compra);
        assertNotNull(criada.getId());
        compraId = criada.getId();
        assertEquals(0, new BigDecimal("1620.00").compareTo(criada.getTotalCompra()));

        // Verifica que COTAÇÃO NÃO alterou estoque inicial (que era 10)
        ProdutoDTO prodAposCotacao = produtoService.findById(produtoId);
        assertEquals(0, new BigDecimal("10.00").compareTo(prodAposCotacao.getEstoqueAtual()));

        // 2. Avança para PEDIDO
        compraService.alterarStatus(compraId, CompraStatus.PEDIDO);

        // 3. Avança para RECEBIMENTO (efetiva entrada no estoque)
        compraService.alterarStatus(compraId, CompraStatus.RECEBIMENTO);
        ProdutoDTO prodAposRecebimento = produtoService.findById(produtoId);

        // Estoque atual deve ser 10 + 20 = 30
        assertEquals(0, new BigDecimal("30.00").compareTo(prodAposRecebimento.getEstoqueAtual()));
        // Preço de custo deve ter sido atualizado para R$ 80.00
        assertEquals(0, new BigDecimal("80.00").compareTo(prodAposRecebimento.getPrecoCusto()));

        // 4. Avança para PAGAMENTO (faturamento e geração de Contas a Pagar)
        compraService.alterarStatus(compraId, CompraStatus.PAGAMENTO);

        // Verifica que gerou Titulo DESPESA em obs_titulo
        List<TituloDTO> titulosCompra = tituloService.findAll(null, TipoTitulo.DESPESA, TituloStatus.PENDENTE, null, null);
        TituloDTO tituloCompra = titulosCompra.stream()
                .filter(t -> compraId.equals(t.getIdCompra()))
                .findFirst()
                .orElse(null);

        assertNotNull(tituloCompra, "Título de Contas a Pagar deve ser gerado automaticamente pela Compra");
        assertEquals(0, new BigDecimal("1620.00").compareTo(tituloCompra.getValorTotal()));
        assertEquals(TipoTitulo.DESPESA, tituloCompra.getTipo());
        assertEquals(TituloStatus.PENDENTE, tituloCompra.getStatus());

        tituloPagarId = tituloCompra.getId();
    }

    @Test
    @Order(6)
    void teste6_CicloVendaEReservaEBaixaEstoqueEContasReceber() {
        assertNotNull(clienteId);
        assertNotNull(produtoId);

        VendaDTO venda = new VendaDTO();
        venda.setIdCliente(clienteId);
        venda.setEmissao(LocalDate.now());
        venda.setPrevisao(LocalDate.now().plusDays(5));
        venda.setStatusVenda(VendaStatus.ABERTO); // Inicializa ABERTO (reserva)
        venda.setGerarTituloFinanceiro(true);

        ItemVendaDTO item = new ItemVendaDTO();
        item.setIdProduto(produtoId);
        item.setQuantidadeItem(new BigDecimal("5.00"));
        item.setPrecoUnitario(new BigDecimal("200.00"));
        item.setValorDesconto(new BigDecimal("50.00"));
        item.setValorAcrescimo(BigDecimal.ZERO);

        List<ItemVendaDTO> itens = new ArrayList<>();
        itens.add(item);
        venda.setItens(itens);

        // 1. Criação da Venda em ABERTO: total = (5 * 200) - 50 = 950.00
        VendaDTO criada = vendaService.create(venda);
        assertNotNull(criada.getId());
        vendaId = criada.getId();
        assertEquals(0, new BigDecimal("950.00").compareTo(criada.getTotalVenda()));

        // Verifica reserva de estoque:
        // Estoque Atual = 30; Reservado = 5; Disponível = 30 - 5 = 25
        ProdutoDTO prodAposVendaAberta = produtoService.findById(produtoId);
        assertEquals(0, new BigDecimal("30.00").compareTo(prodAposVendaAberta.getEstoqueAtual()));
        assertEquals(0, new BigDecimal("5.00").compareTo(prodAposVendaAberta.getEstoqueReservado()));
        assertEquals(0, new BigDecimal("25.00").compareTo(prodAposVendaAberta.getEstoqueDisponivel()));

        // 2. Avança para SEPARACAO
        vendaService.alterarStatus(vendaId, VendaStatus.SEPARACAO);

        // 3. Finaliza Venda (baixa definitiva no estoque + gera Titulo RECEITA)
        vendaService.alterarStatus(vendaId, VendaStatus.FINALIZADO);

        ProdutoDTO prodFinal = produtoService.findById(produtoId);
        // Estoque atual deve ter baixado de 30 para 25
        assertEquals(0, new BigDecimal("25.00").compareTo(prodFinal.getEstoqueAtual()));
        // Reserva liberada (0)
        assertEquals(0, new BigDecimal("0.00").compareTo(prodFinal.getEstoqueReservado()));
        // Disponível = 25
        assertEquals(0, new BigDecimal("25.00").compareTo(prodFinal.getEstoqueDisponivel()));

        // Verifica que gerou Titulo RECEITA em obs_titulo
        List<TituloDTO> titulosVenda = tituloService.findAll(null, TipoTitulo.RECEITA, TituloStatus.PENDENTE, null, null);
        TituloDTO tituloVenda = titulosVenda.stream()
                .filter(t -> vendaId.equals(t.getIdVenda()))
                .findFirst()
                .orElse(null);

        assertNotNull(tituloVenda, "Título de Contas a Receber deve ser gerado automaticamente pela Venda");
        assertEquals(0, new BigDecimal("950.00").compareTo(tituloVenda.getValorTotal()));
        assertEquals(TipoTitulo.RECEITA, tituloVenda.getTipo());
        assertEquals(TituloStatus.PENDENTE, tituloVenda.getStatus());

        tituloReceberId = tituloVenda.getId();
    }

    @Test
    @Order(7)
    void teste7_CicloFinanceiroLiquidacaoEFluxoCaixa() {
        assertNotNull(tituloReceberId);
        assertNotNull(tituloPagarId);

        // 1. Validação de Indicadores de Caixa
        ResumoFinanceiroDTO resumo = tituloService.obterResumo();
        assertNotNull(resumo);
        assertTrue(resumo.getTotalReceberPendente().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(resumo.getTotalPagarPendente().compareTo(BigDecimal.ZERO) > 0);

        // 2. Liquidação (Baixa) da Conta a Receber
        TituloDTO recebido = tituloService.liquidar(tituloReceberId, LocalDate.now());
        assertEquals(TituloStatus.PAGO, recebido.getStatus());
        assertEquals(LocalDate.now(), recebido.getPagamento());

        // 3. Liquidação (Baixa) da Conta a Pagar
        TituloDTO pago = tituloService.liquidar(tituloPagarId, LocalDate.now());
        assertEquals(TituloStatus.PAGO, pago.getStatus());
        assertEquals(LocalDate.now(), pago.getPagamento());

        // 4. Teste de Lançamento Manual Avulso (Despesa Operacional)
        TituloDTO despesaManual = new TituloDTO();
        despesaManual.setTipo(TipoTitulo.DESPESA);
        despesaManual.setIdPessoa(fornecedorService.findById(fornecedorId).getIdPessoa());
        despesaManual.setDescricao("Energia Elétrica E2E " + TIMESTAMP);
        despesaManual.setValorTotal(new BigDecimal("350.00"));
        despesaManual.setEmissao(LocalDate.now());
        despesaManual.setVencimento(LocalDate.now().plusDays(20));

        TituloDTO manualCriado = tituloService.create(despesaManual);
        assertNotNull(manualCriado.getId());
        assertEquals(TituloStatus.PENDENTE, manualCriado.getStatus());

        // 5. Teste de Cancelamento de Título
        TituloDTO cancelado = tituloService.cancelar(manualCriado.getId());
        assertEquals(TituloStatus.CANCELADO, cancelado.getStatus());

        // 6. Teste de Estorno de Quitação
        TituloDTO estornado = tituloService.estornar(tituloReceberId);
        assertEquals(TituloStatus.PENDENTE, estornado.getStatus());
        assertNull(estornado.getPagamento());
    }
}

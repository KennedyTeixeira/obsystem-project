package com.obsystem.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;


public class Titulo {
	
	private Integer id;
	private TipoTitulo tipo;
	private String descricao;
	private Pessoa pessoa;
	private Venda venda;
	private Compra compra;
	private Classificacao classificacao;
	private PlanoConta planoConta;
	private CentroCusto centroCusto;
	private LocalDate emissao;
	private LocalDate vencimento;
	private LocalDate pagamento;
	private BigDecimal valorTotal = BigDecimal.ZERO;
	private TituloStatus status;
	
	public Titulo() {		
	}

	public Titulo(TipoTitulo tipo, String descricao, Pessoa pessoa, Venda venda, Compra compra,
			Classificacao classificacao, PlanoConta planoConta, CentroCusto centroCusto, LocalDate emissao,
			LocalDate vencimento, LocalDate pagamento, BigDecimal valorTotal, TituloStatus status) {		
		this.tipo = tipo;
		this.descricao = descricao;
		this.pessoa = pessoa;
		this.venda = venda;
		this.compra = compra;
		this.classificacao = classificacao;
		this.planoConta = planoConta;
		this.centroCusto = centroCusto;
		this.emissao = emissao;
		this.vencimento = vencimento;
		this.pagamento = pagamento;
		this.valorTotal = valorTotal;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoTitulo getTipo() {
		return tipo;
	}

	public void setTipo(TipoTitulo tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Classificacao classificacao) {
		this.classificacao = classificacao;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public LocalDate getEmissao() {
		return emissao;
	}

	public void setEmissao(LocalDate emissao) {
		this.emissao = emissao;
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}

	public LocalDate getPagamento() {
		return pagamento;
	}

	public void setPagamento(LocalDate pagamento) {
		this.pagamento = pagamento;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public TituloStatus getStatus() {
		return status;
	}

	public void setStatus(TituloStatus status) {
		this.status = status;
	}
	
	public void liquidar(LocalDate dataPagamento) {
	    this.pagamento = dataPagamento != null ? dataPagamento : LocalDate.now();
	    this.status = TituloStatus.PAGO;
	}
	
	public boolean isVencido() {
	    if (this.status == TituloStatus.PENDENTE && this.vencimento != null) {
	        return LocalDate.now().isAfter(this.vencimento);
	    }
	    return false;
	}
	
	public void cancelar() {
	    this.status = TituloStatus.CANCELADO;
	}
	
	// Título que nasce de uma Venda (Contas a Receber):
	public Titulo(Venda venda, LocalDate vencimento) {
	    this.tipo = TipoTitulo.RECEITA;
	    this.venda = venda;
	    this.pessoa = venda.getCliente().getPessoa(); // Já puxa o cliente da venda!
	    this.valorTotal = venda.getTotalVenda();       // Já puxa o valor da venda!
	    this.emissao = venda.getEmissao();
	    this.vencimento = vencimento;
	    this.status = TituloStatus.PENDENTE;
	    this.descricao = "Venda nº " + venda.getId();
	}
	
	// Título que nasce de uma Compra (Contas a Pagar):
	public Titulo(Compra compra, LocalDate vencimento) {
	    this.tipo = TipoTitulo.DESPESA;
	    this.compra = compra;
	    this.pessoa = compra.getFornecedor().getPessoa(); // Já puxa o fornecedor!
	    this.valorTotal = compra.getTotalCompra();        // Já puxa o valor da compra!
	    this.emissao = compra.getEmissao();
	    this.vencimento = vencimento;
	    this.status = TituloStatus.PENDENTE;
	    this.descricao = "Compra nº " + compra.getId();
	}
		
}

package com.obsystem.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_titulo")
public class Titulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_titulo")
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tp_titulo", length = 20, nullable = false)
	private TipoTitulo tipo;
	
	@Column(name = "ds_titulo", length = 100, nullable = false)
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name = "id_pessoa", nullable = false)
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name = "id_venda")
	private Venda venda;
	
	@ManyToOne
	@JoinColumn(name = "id_compra")
	private Compra compra;
	
	@ManyToOne
	@JoinColumn(name = "id_classificacao")
	private Classificacao classificacao;
	
	@ManyToOne
	@JoinColumn(name = "id_plano_conta")
	private PlanoConta planoConta;
	
	@ManyToOne
	@JoinColumn(name = "id_centro_custo")
	private CentroCusto centroCusto;
	
	@Column(name = "dt_emissao", nullable = false)
	private LocalDate emissao;
	
	@Column(name = "dt_vencimento", nullable = false)
	private LocalDate vencimento;
	
	@Column(name = "dt_pagamento")
	private LocalDate pagamento;
	
	@Column(name = "vl_total_titulo", precision = 12, scale = 2, nullable = false)
	private BigDecimal valorTotal = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_titulo", length = 20, nullable = false)
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

	// Título que nasce de uma Venda (Contas a Receber):
	public Titulo(Venda venda, LocalDate vencimento) {
	    this.tipo = TipoTitulo.RECEITA;
	    this.venda = venda;
	    this.pessoa = venda.getCliente().getPessoa(); // Puxa a pessoa do cliente da venda
	    this.valorTotal = venda.getTotalVenda();       // Puxa o total da venda
	    this.emissao = venda.getEmissao();
	    this.vencimento = vencimento;
	    this.status = TituloStatus.PENDENTE;
	    this.descricao = "Venda nº " + venda.getId();
	}
	
	// Título que nasce de uma Compra (Contas a Pagar):
	public Titulo(Compra compra, LocalDate vencimento) {
	    this.tipo = TipoTitulo.DESPESA;
	    this.compra = compra;
	    this.pessoa = compra.getFornecedor().getPessoa(); // Puxa a pessoa do fornecedor
	    this.valorTotal = compra.getTotalCompra();        // Puxa o total da compra
	    this.emissao = compra.getEmissao();
	    this.vencimento = vencimento;
	    this.status = TituloStatus.PENDENTE;
	    this.descricao = "Compra nº " + compra.getId();
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Titulo other = (Titulo) obj;
		return Objects.equals(id, other.id);
	}	
}
package com.obsystem.entities;

import java.math.BigDecimal;
import java.util.Objects;

import com.obsystem.entities.enums.FornecedorStatus;
import com.obsystem.entities.enums.TipoPagamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_fornecedor")
public class Fornecedor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fornecedor")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_pessoa", nullable = false, unique = true)
	private Pessoa pessoa;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_fornecedor", length = 20, nullable = false)
	private FornecedorStatus status;
	
	@Column(name = "vl_credito", precision = 12, scale = 2, nullable = false)
	private BigDecimal credito;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tp_pagamento", length = 20, nullable = false)
	private TipoPagamento pagamento;
	
	public Fornecedor() {		
	}

	public Fornecedor(Pessoa pessoa) {		
		this.pessoa = pessoa;
		this.status = FornecedorStatus.ATIVO;
		this.credito = BigDecimal.ZERO;		
		this.pagamento = TipoPagamento.PIX;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public FornecedorStatus getStatus() {
		return status;
	}

	public void setStatus(FornecedorStatus status) {
		this.status = status;
	}

	public BigDecimal getCredito() {
		return credito;
	}

	public void setCredito(BigDecimal credito) {
		this.credito = credito;
	}

	public TipoPagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(TipoPagamento pagamento) {
		this.pagamento = pagamento;
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
		Fornecedor other = (Fornecedor) obj;
		return Objects.equals(id, other.id);
	}
}
package com.obsystem.entities;

import java.math.BigDecimal;
import com.obsystem.entities.enums.ClienteStatus;


public class Cliente {
	
	private Integer id;
	private Pessoa pessoa;
	private ClienteStatus status;
	private BigDecimal saldo;
	private BigDecimal limite;
	
	public Cliente() {
		
	}

	public Cliente(Pessoa pessoa) {		
		this.pessoa = pessoa;
		this.status = ClienteStatus.ATIVO;
		this.saldo = BigDecimal.ZERO;
		this.limite = BigDecimal.ZERO;
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

	public ClienteStatus getStatus() {
		return status;
	}

	public void setStatus(ClienteStatus status) {
		this.status = status;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}	
	
}

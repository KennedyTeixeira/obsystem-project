package com.obsystem.entities;

import java.math.BigDecimal;
import java.util.Objects;

import com.obsystem.entities.enums.ClienteStatus;

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
@Table(name = "obs_cliente")
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "id_pessoa", nullable = false, unique = true)
	private Pessoa pessoa;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_cliente", length = 25, nullable = false)
	private ClienteStatus status;
	
	@Column(name = "vl_saldo", precision = 12, scale = 2, nullable = false)
	private BigDecimal saldo;
	
	@Column(name = "vl_limite", precision = 12, scale = 2, nullable = false)
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
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}	
}
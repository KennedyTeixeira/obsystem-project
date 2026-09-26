package com.obsystem.entities;

import com.obsystem.entities.enums.CentroCustoStatus;
import com.obsystem.entities.enums.TipoCentroCusto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_centro_custo")
public class CentroCusto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_centro_custo")
	private Integer id;
	
	@Column(name = "ds_centro_custo", length = 50, nullable = false)
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tp_centro_custo", length = 20, nullable = false)
	private TipoCentroCusto tipo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_centro_custo", length = 10, nullable = false)
	private CentroCustoStatus status;
	
	public CentroCusto() {		
	}

	public CentroCusto(String descricao, TipoCentroCusto tipo, CentroCustoStatus status) {		
		this.descricao = descricao;
		this.tipo = tipo;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoCentroCusto getTipo() {
		return tipo;
	}

	public void setTipo(TipoCentroCusto tipo) {
		this.tipo = tipo;
	}

	public CentroCustoStatus getStatus() {
		return status;
	}

	public void setStatus(CentroCustoStatus status) {
		this.status = status;
	}
}

package com.obsystem.entities;

import com.obsystem.entities.enums.CentroCustoStatus;
import com.obsystem.entities.enums.TipoCentroCusto;

public class CentroCusto {
	
	private Integer id;
	private String descricao;
	private TipoCentroCusto tipo;
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

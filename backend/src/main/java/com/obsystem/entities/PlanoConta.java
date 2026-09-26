package com.obsystem.entities;

import com.obsystem.entities.enums.PlanoContaStatus;
import com.obsystem.entities.enums.TipoPlanoConta;

public class PlanoConta {
	
	private Integer id;
	private String descricao;
	private TipoPlanoConta tipo;
	private PlanoContaStatus status;
	
	public PlanoConta() {		
	}

	public PlanoConta(String descricao, TipoPlanoConta tipo, PlanoContaStatus status) {		
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

	public TipoPlanoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoPlanoConta tipo) {
		this.tipo = tipo;
	}

	public PlanoContaStatus getStatus() {
		return status;
	}

	public void setStatus(PlanoContaStatus status) {
		this.status = status;
	}	
}

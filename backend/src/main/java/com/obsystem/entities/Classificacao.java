package com.obsystem.entities;

import com.obsystem.entities.enums.ClassificacaoStatus;
import com.obsystem.entities.enums.TipoClassificacao;

public class Classificacao {
	
	private Integer id;
	private String descricao;
	private TipoClassificacao tipo;
	private ClassificacaoStatus status;
	
	public Classificacao() {		
	}

	public Classificacao(String descricao, TipoClassificacao tipo, ClassificacaoStatus status) {		
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

	public TipoClassificacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoClassificacao tipo) {
		this.tipo = tipo;
	}

	public ClassificacaoStatus getStatus() {
		return status;
	}

	public void setStatus(ClassificacaoStatus status) {
		this.status = status;
	}	
}
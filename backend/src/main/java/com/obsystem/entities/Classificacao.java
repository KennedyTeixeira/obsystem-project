package com.obsystem.entities;

import com.obsystem.entities.enums.ClassificacaoStatus;
import com.obsystem.entities.enums.TipoClassificacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_classificacao")

public class Classificacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_classificacao")
	private Integer id;

	@Column(name = "ds_classificacao", length = 50, nullable = false)
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(name = "tp_classificacao", length = 25, nullable = false)
	private TipoClassificacao tipo;

	@Enumerated(EnumType.STRING)
	@Column(name = "st_classificacao", length = 10, nullable = false)
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
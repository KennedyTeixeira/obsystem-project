package com.obsystem.entities;

import java.util.Objects;

import com.obsystem.entities.enums.PlanoContaStatus;
import com.obsystem.entities.enums.TipoPlanoConta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_plano_conta")
public class PlanoConta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_plano_conta")
	private Integer id;

	@Column(name = "ds_plano_conta", length = 50, nullable = false)
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(name = "tp_plano_conta", length = 20, nullable = false)
	private TipoPlanoConta tipo;

	@Enumerated(EnumType.STRING)
	@Column(name = "st_plano_conta", length = 10, nullable = false)
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
		PlanoConta other = (PlanoConta) obj;
		return Objects.equals(id, other.id);
	}

}

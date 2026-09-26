package com.obsystem.entities;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pessoa")
	private Integer id;
	
	@Column(name = "nm_pessoa", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "tp_pessoa", length = 1, nullable = false)
	private Character tipo;
	
	@Column(name = "ds_segmento", length = 50)
	private String segmento;
	
	@Column(name = "ds_email", length = 100)
	private String email;
	
	@Column(name = "dt_cadastro")
	private LocalDate cadastro;
	
	@Column(name = "ds_site", length = 100)
	private String site;
	
	@Column(name = "ds_observacao", columnDefinition = "TEXT")
	private String observacao;
	
	public Pessoa() {		
	}

	public Pessoa(String nome, Character tipo, String segmento, String email, LocalDate cadastro, String site, String observacao) {		
		this.nome = nome;
		this.tipo = tipo;
		this.segmento = segmento;
		this.email = email;
		this.cadastro = cadastro;
		this.site = site;
		this.observacao = observacao;
	}	

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getCadastro() {
		return cadastro;
	}

	public void setCadastro(LocalDate cadastro) {
		this.cadastro = cadastro;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}	
}
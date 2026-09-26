package com.obsystem.entities;

import java.time.LocalDate;

import com.obsystem.entities.enums.Genero;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_fisica")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Fisica extends Pessoa {
	
	@Column(name = "nr_cpf", length = 14, unique = true)
	private String cpf;
	
	@Column(name = "nr_rg", length = 20)
	private String rg;
	
	@Column(name = "sg_uf_rg", length = 2)
	private String uf;
	
	@Column(name = "dt_emissao_rg")
	private LocalDate emissao;
	
	@Column(name = "ds_emissor_rg", length = 20)
	private String emissor;
	
	@Column(name = "ds_naturalidade", length = 50)
	private String naturalidade;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tp_genero", length = 15)
	private Genero genero;
	
	public Fisica() {
		super();
	}

	public Fisica(String nome, String segmento, String email, LocalDate cadastro, String site,
			String observacao, String cpf, String rg, String uf, LocalDate emissao, String emissor,
			String naturalidade, Genero genero) {
		super(nome, 'F', segmento, email, cadastro, site, observacao);
		this.cpf = cpf;
		this.rg = rg;
		this.uf = uf;
		this.emissao = emissao;
		this.emissor = emissor;
		this.naturalidade = naturalidade;
		this.genero = genero;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public LocalDate getEmissao() {
		return emissao;
	}

	public void setEmissao(LocalDate emissao) {
		this.emissao = emissao;
	}

	public String getEmissor() {
		return emissor;
	}

	public void setEmissor(String emissor) {
		this.emissor = emissor;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}		
}
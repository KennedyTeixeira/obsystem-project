package com.obsystem.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_juridica")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class Juridica extends Pessoa {
	
	@Column(name = "nr_cnpj", length = 18, unique = true)
	private String cnpj;
	
	@Column(name = "nm_fantasia", length = 100)
	private String fantasia;
	
	@Column(name = "nr_ie", length = 20)
	private String ie;
	
	@Column(name = "nr_im", length = 20)
	private String im;	
	
	public Juridica() {
		super();
	}

	public Juridica(String nome, String segmento, String email, LocalDate cadastro, String site,
			String observacao, String cnpj, String fantasia, String ie, String im) {
		super(nome, 'J', segmento, email, cadastro, site, observacao);
		this.cnpj = cnpj;
		this.fantasia = fantasia;
		this.ie = ie;
		this.im = im;
	}

	public String getCpf() {
		return cnpj; // ou getCnpj
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getFantasia() {
		return fantasia;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}	
}
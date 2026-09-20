package entities;

import java.time.LocalDate;
import entities.enums.Genero;

public class Fisica extends Pessoa {
	
	private String cpf;
	private String rg;
	private String uf;
	private LocalDate emissao;
	private String emissor;
	private String naturalidade;
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

package entities;

import java.time.LocalDate;

public class Pessoa {
	
	private Integer id;
	private String nome;
	private Character tipo;
	private String segmento;
	private String email;
	private LocalDate cadastro;
	private String site;
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
	
}


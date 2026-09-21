package entities;

import entities.enums.TipoContato;

public class Contato {
	
	private Integer id;
	private Pessoa pessoa;
	private TipoContato tipo;
	private String numero;
	
	public Contato() {
		
	}

	public Contato(Pessoa pessoa, TipoContato tipo, String numero) {		
		this.pessoa = pessoa;
		this.tipo = tipo;
		this.numero = numero;
	}	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public TipoContato getTipo() {
		return tipo;
	}

	public void setTipo(TipoContato tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}	
		
}

package entities;

import java.time.LocalDate;

public class Juridica extends Pessoa{
	
	private String cnpj;
	private String fantasia;
	private String ie;
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

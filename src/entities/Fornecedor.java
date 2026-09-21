package entities;

import java.math.BigDecimal;
import entities.enums.FornecedorStatus;
import entities.enums.TipoPagamento;

public class Fornecedor {
	
	private Integer id;
	private Pessoa pessoa;
	private FornecedorStatus status;
	private BigDecimal credito;	
	private TipoPagamento pagamento;
	
	public Fornecedor() {		
	}

	public Fornecedor(Pessoa pessoa) {		
		this.pessoa = pessoa;
		this.status = FornecedorStatus.ATIVO;
		this.credito = BigDecimal.ZERO;		
		this.pagamento = TipoPagamento.PIX;
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

	public FornecedorStatus getStatus() {
		return status;
	}

	public void setStatus(FornecedorStatus status) {
		this.status = status;
	}

	public BigDecimal getCredito() {
		return credito;
	}

	public void setCredito(BigDecimal credito) {
		this.credito = credito;
	}

	public TipoPagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(TipoPagamento pagamento) {
		this.pagamento = pagamento;
	}	

}

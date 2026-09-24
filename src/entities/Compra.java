package entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entities.enums.CompraStatus;

public class Compra {
	
	private Integer id;
	private Fornecedor fornecedor;
	private LocalDate emissao;
	private LocalDate previsao;
	private List<ItemCompra> itens = new ArrayList<>();
	private BigDecimal totalCompra = BigDecimal.ZERO;
	private CompraStatus statusCompra;
	
	public Compra() {		
	}

	public Compra(Fornecedor fornecedor, LocalDate emissao, LocalDate previsao, CompraStatus statusCompra) {		
	    this.fornecedor = fornecedor;
	    this.emissao = emissao;
	    this.previsao = previsao;		
	    this.statusCompra = statusCompra;
	}
	
	public Compra(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
		this.emissao = LocalDate.now();
		this.previsao = LocalDate.now();
		this.statusCompra = CompraStatus.REQUISICAO;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public LocalDate getEmissao() {
		return emissao;
	}

	public void setEmissao(LocalDate emissao) {
		this.emissao = emissao;
	}

	public LocalDate getPrevisao() {
		return previsao;
	}

	public void setPrevisao(LocalDate previsao) {
		this.previsao = previsao;
	}

	public List<ItemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompra> itens) {
		this.itens = itens;
	}

	public BigDecimal getTotalCompra() {
		return totalCompra;
	}

	public void setTotalCompra(BigDecimal totalCompra) {
		this.totalCompra = totalCompra;
	}

	public CompraStatus getStatusCompra() {
		return statusCompra;
	}

	public void setStatusCompra(CompraStatus statusCompra) {
		this.statusCompra = statusCompra;
	}
	
	public void adicionarItem(ItemCompra item) {
	    item.setCompra(this);
	    item.setNumeroItem(this.itens.size() + 1);
	    this.itens.add(item);	    
	    
	    if (item.getValorTotal() != null) {
	        this.totalCompra = this.totalCompra.add(item.getValorTotal());
	    }
	}
	
	public void removerItem(ItemCompra item) {
	    if (this.itens.remove(item)) {
	        this.totalCompra = this.totalCompra.subtract(item.getValorTotal());
	    }
	}
	
	public BigDecimal calcularTotalCompra() {
	    BigDecimal soma = BigDecimal.ZERO;
	    for (ItemCompra item : this.itens) {
	        soma = soma.add(item.getValorTotal());
	    }
	    this.totalCompra = soma;
	    return this.totalCompra;
	}
	
}

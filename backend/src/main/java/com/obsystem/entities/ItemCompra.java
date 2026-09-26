package com.obsystem.entities;

import java.math.BigDecimal;
import com.obsystem.entities.enums.ItemStatus;

public class ItemCompra {
	
	private Integer id;
	private Compra compra;
	private Produto produto;
	private Integer numeroItem;
	private BigDecimal quantidadeItem;
	private BigDecimal precoUnitario;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorDespesa = BigDecimal.ZERO;
	private BigDecimal valorTotal = BigDecimal.ZERO;
	private ItemStatus statusItem = ItemStatus.INCLUSO;
	
	public ItemCompra() {		
	}

	public ItemCompra(Compra compra, Produto produto, Integer numeroItem, BigDecimal quantidadeItem,
			BigDecimal precoUnitario, BigDecimal valorDesconto, BigDecimal valorDespesa, BigDecimal valorTotal,
			ItemStatus statusItem) {		
		this.compra = compra;
		this.produto = produto;
		this.numeroItem = numeroItem;
		this.quantidadeItem = quantidadeItem;
		this.precoUnitario = precoUnitario;
		this.valorDesconto = valorDesconto;
		this.valorDespesa = valorDespesa;
		this.valorTotal = valorTotal;
		this.statusItem = statusItem;
	}
	
	public ItemCompra(Produto produto, BigDecimal quantidadeItem, BigDecimal precoUnitario) {
	    this.produto = produto;
	    this.quantidadeItem = quantidadeItem;
	    this.precoUnitario = precoUnitario != null ? precoUnitario : produto.getPrecoCusto();
	    this.calcularValorTotal();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getNumeroItem() {
		return numeroItem;
	}

	public void setNumeroItem(Integer numeroItem) {
		this.numeroItem = numeroItem;
	}

	public BigDecimal getQuantidadeItem() {
		return quantidadeItem;
	}

	public void setQuantidadeItem(BigDecimal quantidadeItem) {
		this.quantidadeItem = quantidadeItem;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorDespesa() {
		return valorDespesa;
	}

	public void setValorDespesa(BigDecimal valorDespesa) {
		this.valorDespesa = valorDespesa;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public ItemStatus getStatusItem() {
		return statusItem;
	}

	public void setStatusItem(ItemStatus statusItem) {
		this.statusItem = statusItem;
	}
	
	public BigDecimal calcularValorTotal() {
	    if (this.precoUnitario != null && this.quantidadeItem != null) {
	        this.valorTotal = this.precoUnitario.multiply(this.quantidadeItem)
	                                            .subtract(this.valorDesconto)
	                                            .add(this.valorDespesa);
	    }
	    return this.valorTotal;
	}
}

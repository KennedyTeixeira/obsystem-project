package entities;

import java.math.BigDecimal;

import entities.enums.ItemStatus;

public class ItemVenda {

	private Integer id;
	private Venda venda;
	private Produto produto;
	private Integer numeroItem;
	private BigDecimal quantidadeItem;
	private BigDecimal precoUnitario;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorAcrescimo = BigDecimal.ZERO;
	private BigDecimal valorTotal;
	private BigDecimal valorCusto;
	private ItemStatus statusItem = ItemStatus.INCLUSO;

	public ItemVenda() {
	}

	public ItemVenda(Venda venda, Produto produto, Integer numeroItem, BigDecimal quantidadeItem,
			BigDecimal valorDesconto, BigDecimal valorAcrescimo) {
		this.venda = venda;
		this.produto = produto;
		this.numeroItem = numeroItem;
		this.quantidadeItem = quantidadeItem;
		this.precoUnitario = produto.getPrecoVenda();
		this.valorDesconto = valorDesconto;
		this.valorAcrescimo = valorAcrescimo;		
		this.valorCusto = produto.getPrecoCusto();
		this.calcularValorTotal();
	}
	
	public ItemVenda(Produto produto, BigDecimal quantidadeItem) {
	    this.produto = produto;
	    this.quantidadeItem = quantidadeItem;
	    this.precoUnitario = produto.getPrecoVenda();
	    this.valorCusto = produto.getPrecoCusto();
	    this.calcularValorTotal();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
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

	public BigDecimal getValorAcrescimo() {
		return valorAcrescimo;
	}

	public void setValorAcrescimo(BigDecimal valorAcrescimo) {
		this.valorAcrescimo = valorAcrescimo;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public ItemStatus getStatusItem() {
		return statusItem;
	}

	public void setStatusItem(ItemStatus statusItem) {
		this.statusItem = statusItem;
	}

	public BigDecimal calcularValorTotal() {
		this.valorTotal = this.precoUnitario.multiply(this.quantidadeItem).subtract(this.valorDesconto)
				.add(this.valorAcrescimo);
		return this.valorTotal;
	}
}

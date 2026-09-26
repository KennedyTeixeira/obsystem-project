package com.obsystem.entities;

import java.math.BigDecimal;
import java.util.Objects;

import com.obsystem.entities.enums.ItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_item_compra")
public class ItemCompra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item_compra")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_compra", nullable = false)
	private Compra compra;

	@ManyToOne
	@JoinColumn(name = "id_produto", nullable = false)
	private Produto produto;

	@Column(name = "nr_item", nullable = false)
	private Integer numeroItem;

	@Column(name = "qt_item", precision = 12, scale = 3, nullable = false)
	private BigDecimal quantidadeItem;

	@Column(name = "vl_unitario", precision = 12, scale = 2, nullable = false)
	private BigDecimal precoUnitario;

	@Column(name = "vl_desconto", precision = 12, scale = 2, nullable = false)
	private BigDecimal valorDesconto = BigDecimal.ZERO;

	@Column(name = "vl_despesa", precision = 12, scale = 2, nullable = false)
	private BigDecimal valorDespesa = BigDecimal.ZERO;

	@Column(name = "vl_total", precision = 12, scale = 2, nullable = false)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	@Column(name = "st_item", length = 20, nullable = false)
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemCompra other = (ItemCompra) obj;
		return Objects.equals(id, other.id);
	}
}
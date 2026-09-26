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
@Table(name = "obs_item_venda")
public class ItemVenda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item_venda")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_venda", nullable = false)
	private Venda venda;

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

	@Column(name = "vl_acrescimo", precision = 12, scale = 2, nullable = false)
	private BigDecimal valorAcrescimo = BigDecimal.ZERO;

	@Column(name = "vl_total", precision = 12, scale = 2, nullable = false)
	private BigDecimal valorTotal;

	@Column(name = "vl_custo", precision = 12, scale = 2)
	private BigDecimal valorCusto;

	@Enumerated(EnumType.STRING)
	@Column(name = "st_item", length = 20, nullable = false)
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
		ItemVenda other = (ItemVenda) obj;
		return Objects.equals(id, other.id);
	}
}
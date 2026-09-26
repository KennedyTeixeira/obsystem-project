package com.obsystem.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.obsystem.entities.enums.CompraStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_compra")
public class Compra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_compra")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_fornecedor", nullable = false)
	private Fornecedor fornecedor;
	
	@Column(name = "dt_emissao", nullable = false)
	private LocalDate emissao;
	
	@Column(name = "dt_previsao")
	private LocalDate previsao;
	
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemCompra> itens = new ArrayList<>();
	
	@Column(name = "vl_total_compra", precision = 12, scale = 2, nullable = false)
	private BigDecimal totalCompra = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_compra", length = 20, nullable = false)
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
		Compra other = (Compra) obj;
		return Objects.equals(id, other.id);
	}	
}
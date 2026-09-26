package com.obsystem.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.obsystem.entities.enums.VendaStatus;

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
@Table(name = "obs_venda")
public class Venda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_venda")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;
	
	@Column(name = "dt_emissao", nullable = false)
	private LocalDate emissao;
	
	@Column(name = "dt_previsao")
	private LocalDate previsao;
	
	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemVenda> itens = new ArrayList<>();
	
	@Column(name = "vl_total_venda", precision = 12, scale = 2, nullable = false)
	private BigDecimal totalVenda = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_venda", length = 20, nullable = false)
	private VendaStatus statusVenda;
	
	public Venda() {		
	}

	public Venda(Cliente cliente, LocalDate emissao, LocalDate previsao, VendaStatus statusVenda) {		
		this.cliente = cliente;
		this.emissao = emissao;
		this.previsao = previsao;		
		this.statusVenda = statusVenda;
	}
	
	public Venda(Cliente cliente) {
		this.cliente = cliente;
		this.emissao = LocalDate.now();
		this.previsao = LocalDate.now();
		this.statusVenda = VendaStatus.ORCAMENTO;
	}		

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getEmissao() {
		return emissao;
	}

	public void setEmissao(LocalDate emissao) {
		this.emissao = emissao;
	}
	
	public List<ItemVenda> getItens() {
	    return itens;
	}

	public LocalDate getPrevisao() {
		return previsao;
	}

	public void setPrevisao(LocalDate previsao) {
		this.previsao = previsao;
	}

	public BigDecimal getTotalVenda() {
		return totalVenda;
	}

	public void setTotalVenda(BigDecimal totalVenda) {
		this.totalVenda = totalVenda;
	}

	public VendaStatus getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(VendaStatus statusVenda) {
		this.statusVenda = statusVenda;
	}
	
	public void adicionarItem(ItemVenda item) {
	    item.setVenda(this);
	    item.setNumeroItem(this.itens.size() + 1);
	    this.itens.add(item);	    
	    
	    if (item.getValorTotal() != null) {
	        this.totalVenda = this.totalVenda.add(item.getValorTotal());
	    }
	}
	
	public void removerItem(ItemVenda item) {
	    if (this.itens.remove(item)) {
	        this.totalVenda = this.totalVenda.subtract(item.getValorTotal());
	    }
	}
	
	public BigDecimal calcularTotalVenda() {
	    BigDecimal soma = BigDecimal.ZERO;
	    for (ItemVenda item : this.itens) {
	        soma = soma.add(item.getValorTotal());
	    }
	    this.totalVenda = soma;
	    return this.totalVenda;
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
		Venda other = (Venda) obj;
		return Objects.equals(id, other.id);
	}	
}
package entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entities.enums.VendaStatus;

public class Venda {
	
	private Integer id;
	private Cliente cliente;
	private LocalDate emissao;
	private LocalDate previsao;
	private List<ItemVenda> itens = new ArrayList<>();
	private BigDecimal totalVenda = BigDecimal.ZERO;
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
	
}

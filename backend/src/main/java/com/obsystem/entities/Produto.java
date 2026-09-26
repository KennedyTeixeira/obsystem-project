package com.obsystem.entities;

import java.math.BigDecimal;

import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.ProdutoStatus;
import com.obsystem.entities.enums.ProdutoTipo;

public class Produto {
	
	private Integer id;
	private String nomeProduto;
	private String codigoProduto;
	private BigDecimal precoCusto = BigDecimal.ZERO;
	private BigDecimal precoVenda = BigDecimal.ZERO;
	private BigDecimal estoqueMinimo = BigDecimal.ZERO;
	private BigDecimal estoqueMaximo = BigDecimal.ZERO;
	private BigDecimal estoqueReservado = BigDecimal.ZERO;
	private BigDecimal estoqueAtual = BigDecimal.ZERO;
	private BigDecimal estoqueDisponivel = BigDecimal.ZERO;
	private ProdutoTipo tipoProduto;
	private String unidadeMedida;
	private String categoria;
	private String subcategoria;
	private String modelo;
	private Double milimetro;
	private Integer medida;
	private String marca;
	private ProdutoEstoque controlaEstoque;
	private ProdutoStatus statusProduto = ProdutoStatus.ATIVO;
	private Character fracionar;
	
	public Produto() {		
	}

	public Produto(String nomeProduto, String codigoProduto, BigDecimal precoCusto, BigDecimal precoVenda, BigDecimal estoqueMinimo,
			BigDecimal estoqueMaximo, BigDecimal estoqueReservado, BigDecimal estoqueAtual, BigDecimal estoqueDisponivel,
			ProdutoTipo tipoProduto, String unidadeMedida, String categoria, String subcategoria, String modelo,
			Double milimetro, Integer medida, String marca, ProdutoEstoque controlaEstoque, Character fracionar) {		
		this.nomeProduto = nomeProduto;
		this.codigoProduto = codigoProduto;
		this.precoCusto = precoCusto;
		this.precoVenda = precoVenda;
		this.estoqueMinimo = estoqueMinimo;
		this.estoqueMaximo = estoqueMaximo;
		this.estoqueReservado = estoqueReservado;
		this.estoqueAtual = estoqueAtual;
		this.estoqueDisponivel = estoqueDisponivel;
		this.tipoProduto = tipoProduto;
		this.unidadeMedida = unidadeMedida;
		this.categoria = categoria;
		this.subcategoria = subcategoria;
		this.modelo = modelo;
		this.milimetro = milimetro;
		this.medida = medida;
		this.marca = marca;
		this.controlaEstoque = controlaEstoque;		
		this.fracionar = fracionar;
	}

	public Produto(String nomeProduto, String codigoProduto, BigDecimal precoCusto, BigDecimal precoVenda, ProdutoTipo tipoProduto,
			String unidadeMedida, String categoria, String subcategoria, String modelo, ProdutoEstoque controlaEstoque, Character fracionar) {		
		this.nomeProduto = nomeProduto;
		this.codigoProduto = codigoProduto;
		this.precoCusto = precoCusto;
		this.precoVenda = precoVenda;		
		this.tipoProduto = tipoProduto;
		this.unidadeMedida = unidadeMedida;
		this.categoria = categoria;
		this.subcategoria = subcategoria;
		this.modelo = modelo;
		this.controlaEstoque = controlaEstoque;		
		this.fracionar = fracionar;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public BigDecimal getEstoqueMaximo() {
		return estoqueMaximo;
	}

	public void setEstoqueMaximo(BigDecimal estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}

	public BigDecimal getEstoqueReservado() {
		return estoqueReservado;
	}

	public void setEstoqueReservado(BigDecimal estoqueReservado) {
		this.estoqueReservado = estoqueReservado;
	}

	public BigDecimal getEstoqueAtual() {
		return estoqueAtual;
	}

	public void setEstoqueAtual(BigDecimal estoqueAtual) {
		this.estoqueAtual = estoqueAtual;
	}

	public BigDecimal getEstoqueDisponivel() {
		return estoqueDisponivel;
	}

	public void setEstoqueDisponivel(BigDecimal estoqueDisponivel) {
		this.estoqueDisponivel = estoqueDisponivel;
	}

	public ProdutoTipo getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(ProdutoTipo tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSubcategoria() {
		return subcategoria;
	}

	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Double getMilimetro() {
		return milimetro;
	}

	public void setMilimetro(Double milimetro) {
		this.milimetro = milimetro;
	}

	public Integer getMedida() {
		return medida;
	}

	public void setMedida(Integer medida) {
		this.medida = medida;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public ProdutoEstoque getControlaEstoque() {
		return controlaEstoque;
	}

	public void setControlaEstoque(ProdutoEstoque controlaEstoque) {
		this.controlaEstoque = controlaEstoque;
	}

	public ProdutoStatus getStatusProduto() {
		return statusProduto;
	}

	public void setStatusProduto(ProdutoStatus statusProduto) {
		this.statusProduto = statusProduto;
	}

	public Character getFracionar() {
		return fracionar;
	}

	public void setFracionar(Character fracionar) {
		this.fracionar = fracionar;
	}

	public BigDecimal calcularEstoqueDisponivel() {
	    return this.estoqueAtual.subtract(this.estoqueReservado);
	}
}

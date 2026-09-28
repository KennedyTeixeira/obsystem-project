package com.obsystem.entities;

import java.math.BigDecimal;
import java.util.Objects;

import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.ProdutoStatus;
import com.obsystem.entities.enums.ProdutoTipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_produto")
public class Produto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produto")
	private Integer id;
	
	@Column(name = "nm_produto", length = 100, nullable = false)
	private String nomeProduto;
	
	@Column(name = "cd_produto", length = 50, unique = true)
	private String codigoProduto;
	
	@Column(name = "vl_custo", precision = 12, scale = 2, nullable = false)
	private BigDecimal precoCusto = BigDecimal.ZERO;
	
	@Column(name = "vl_venda", precision = 12, scale = 2, nullable = false)
	private BigDecimal precoVenda = BigDecimal.ZERO;
	
	@Column(name = "qt_estoque_minimo", precision = 12, scale = 3, nullable = false)
	private BigDecimal estoqueMinimo = BigDecimal.ZERO;
	
	@Column(name = "qt_estoque_maximo", precision = 12, scale = 3, nullable = false)
	private BigDecimal estoqueMaximo = BigDecimal.ZERO;
	
	@Column(name = "qt_estoque_reservado", precision = 12, scale = 3, nullable = false)
	private BigDecimal estoqueReservado = BigDecimal.ZERO;
	
	@Column(name = "qt_estoque_atual", precision = 12, scale = 3, nullable = false)
	private BigDecimal estoqueAtual = BigDecimal.ZERO;
	
	@Column(name = "qt_estoque_disponivel", precision = 12, scale = 3, nullable = false)
	private BigDecimal estoqueDisponivel = BigDecimal.ZERO;
	
	@Column(name = "tp_produto", length = 50, nullable = false)
	private String tipoProduto = "PRODUTO";
	
	@Column(name = "sg_unidade_medida", length = 10)
	private String unidadeMedida;
	
	@Column(name = "ds_categoria", length = 50)
	private String categoria;
	
	@Column(name = "ds_subcategoria", length = 50)
	private String subcategoria;

	@Column(name = "ds_classe", length = 50)
	private String classe;
	
	@Column(name = "ds_modelo", length = 50)
	private String modelo;
	
	@Column(name = "nr_milimetro")
	private Double milimetro;
	
	@Column(name = "nr_medida")
	private Integer medida;
	
	@Column(name = "ds_marca", length = 50)
	private String marca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_controla_estoque", length = 10)
	private ProdutoEstoque controlaEstoque;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "st_produto", length = 20, nullable = false)
	private ProdutoStatus statusProduto = ProdutoStatus.ATIVO;
	
	@Column(name = "st_fracionar", length = 1)
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
		this.tipoProduto = tipoProduto != null ? tipoProduto.name() : "PRODUTO";
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

	public Produto(String nomeProduto, String codigoProduto, BigDecimal precoCusto, BigDecimal precoVenda, BigDecimal estoqueMinimo,
			BigDecimal estoqueMaximo, BigDecimal estoqueReservado, BigDecimal estoqueAtual, BigDecimal estoqueDisponivel,
			String tipoProduto, String unidadeMedida, String categoria, String subcategoria, String classe, String modelo,
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
		this.tipoProduto = tipoProduto != null ? tipoProduto : "PRODUTO";
		this.unidadeMedida = unidadeMedida;
		this.categoria = categoria;
		this.subcategoria = subcategoria;
		this.classe = classe;
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
		this.tipoProduto = tipoProduto != null ? tipoProduto.name() : "PRODUTO";
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

	public String getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public void setTipoProduto(ProdutoTipo tipoProduto) {
		this.tipoProduto = tipoProduto != null ? tipoProduto.name() : null;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
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
		Produto other = (Produto) obj;
		return Objects.equals(id, other.id);
	}
}
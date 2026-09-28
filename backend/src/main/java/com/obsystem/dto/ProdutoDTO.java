package com.obsystem.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.obsystem.entities.Produto;
import com.obsystem.entities.enums.ProdutoEstoque;
import com.obsystem.entities.enums.ProdutoStatus;
import com.obsystem.entities.enums.ProdutoTipo;

public class ProdutoDTO {

    private Integer id;
    private String nomeProduto;
    private String codigoProduto;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private BigDecimal estoqueMinimo;
    private BigDecimal estoqueMaximo;
    private BigDecimal estoqueReservado;
    private BigDecimal estoqueAtual;
    private BigDecimal estoqueDisponivel;
    private String tipoProduto;
    private String unidadeMedida;
    private String categoria;
    private String subcategoria;
    private String classe;
    private String modelo;
    private Double milimetro;
    private Integer medida;
    private String marca;
    private ProdutoEstoque controlaEstoque;
    private ProdutoStatus statusProduto;
    private Character fracionar;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Produto entity) {
        this.id = entity.getId();
        this.nomeProduto = entity.getNomeProduto();
        this.codigoProduto = entity.getCodigoProduto();
        this.precoCusto = entity.getPrecoCusto();
        this.precoVenda = entity.getPrecoVenda();
        this.estoqueMinimo = entity.getEstoqueMinimo();
        this.estoqueMaximo = entity.getEstoqueMaximo();
        this.estoqueReservado = entity.getEstoqueReservado();
        this.estoqueAtual = entity.getEstoqueAtual();
        this.estoqueDisponivel = entity.getEstoqueDisponivel();
        this.tipoProduto = entity.getTipoProduto();
        this.unidadeMedida = entity.getUnidadeMedida();
        this.categoria = entity.getCategoria();
        this.subcategoria = entity.getSubcategoria();
        this.classe = entity.getClasse();
        this.modelo = entity.getModelo();
        this.milimetro = entity.getMilimetro();
        this.medida = entity.getMedida();
        this.marca = entity.getMarca();
        this.controlaEstoque = entity.getControlaEstoque();
        this.statusProduto = entity.getStatusProduto();
        this.fracionar = entity.getFracionar();
    }

    public BigDecimal getMargemLucro() {
        if (precoCusto != null && precoVenda != null && precoCusto.compareTo(BigDecimal.ZERO) > 0) {
            return precoVenda.subtract(precoCusto)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(precoCusto, 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public String getAlertaEstoque() {
        if (estoqueAtual != null && estoqueMinimo != null && estoqueAtual.compareTo(estoqueMinimo) <= 0) {
            return "CRITICO";
        }
        if (estoqueAtual != null && estoqueMaximo != null && estoqueMaximo.compareTo(BigDecimal.ZERO) > 0
                && estoqueAtual.compareTo(estoqueMaximo) > 0) {
            return "EXCESSIVO";
        }
        return "NORMAL";
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
}

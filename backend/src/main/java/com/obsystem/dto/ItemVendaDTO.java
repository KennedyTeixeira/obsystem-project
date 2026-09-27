package com.obsystem.dto;

import java.math.BigDecimal;

import com.obsystem.entities.ItemVenda;
import com.obsystem.entities.Produto;
import com.obsystem.entities.enums.ItemStatus;

public class ItemVendaDTO {

    private Integer id;
    private Integer idProduto;
    private String nomeProduto;
    private String codigoProduto;
    private String unidadeMedida;
    private Integer numeroItem;
    private BigDecimal quantidadeItem;
    private BigDecimal precoUnitario;
    private BigDecimal valorDesconto;
    private BigDecimal valorAcrescimo;
    private BigDecimal valorTotal;
    private BigDecimal valorCusto;
    private ItemStatus statusItem;

    public ItemVendaDTO() {
    }

    public ItemVendaDTO(ItemVenda entity) {
        this.id = entity.getId();
        this.numeroItem = entity.getNumeroItem();
        this.quantidadeItem = entity.getQuantidadeItem();
        this.precoUnitario = entity.getPrecoUnitario();
        this.valorDesconto = entity.getValorDesconto();
        this.valorAcrescimo = entity.getValorAcrescimo();
        this.valorTotal = entity.getValorTotal();
        this.valorCusto = entity.getValorCusto();
        this.statusItem = entity.getStatusItem();

        Produto p = entity.getProduto();
        if (p != null) {
            this.idProduto = p.getId();
            this.nomeProduto = p.getNomeProduto();
            this.codigoProduto = p.getCodigoProduto();
            this.unidadeMedida = p.getUnidadeMedida();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
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

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
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
}

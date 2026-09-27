package com.obsystem.dto;

import java.math.BigDecimal;

import com.obsystem.entities.ItemCompra;
import com.obsystem.entities.enums.ItemStatus;

public class ItemCompraDTO {

    private Integer id;
    private Integer idProduto;
    private String nomeProduto;
    private String codigoProduto;
    private String unidadeMedida;
    private Integer numeroItem;
    private BigDecimal quantidadeItem;
    private BigDecimal precoUnitario;
    private BigDecimal valorDesconto;
    private BigDecimal valorDespesa;
    private BigDecimal valorTotal;
    private ItemStatus statusItem;

    public ItemCompraDTO() {
    }

    public ItemCompraDTO(ItemCompra entity) {
        this.id = entity.getId();
        if (entity.getProduto() != null) {
            this.idProduto = entity.getProduto().getId();
            this.nomeProduto = entity.getProduto().getNomeProduto();
            this.codigoProduto = entity.getProduto().getCodigoProduto();
            this.unidadeMedida = entity.getProduto().getUnidadeMedida();
        }
        this.numeroItem = entity.getNumeroItem();
        this.quantidadeItem = entity.getQuantidadeItem();
        this.precoUnitario = entity.getPrecoUnitario();
        this.valorDesconto = entity.getValorDesconto();
        this.valorDespesa = entity.getValorDespesa();
        this.valorTotal = entity.getValorTotal();
        this.statusItem = entity.getStatusItem();
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
}

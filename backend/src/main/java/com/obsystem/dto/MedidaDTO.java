package com.obsystem.dto;

import java.math.BigDecimal;

import com.obsystem.entities.Medida;
import com.obsystem.entities.enums.ProdutoStatus;

public class MedidaDTO {

    private Integer id;
    private String descricao;
    private BigDecimal largura;
    private BigDecimal altura;
    private ProdutoStatus status;

    public MedidaDTO() {
    }

    public MedidaDTO(Medida entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.descricao = entity.getDescricao();
            this.largura = entity.getLargura();
            this.altura = entity.getAltura();
            this.status = entity.getStatus();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getLargura() {
        return largura;
    }

    public void setLargura(BigDecimal largura) {
        this.largura = largura;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public ProdutoStatus getStatus() {
        return status;
    }

    public void setStatus(ProdutoStatus status) {
        this.status = status;
    }
}

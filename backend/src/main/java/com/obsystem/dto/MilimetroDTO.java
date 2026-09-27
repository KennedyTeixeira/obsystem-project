package com.obsystem.dto;

import java.math.BigDecimal;

import com.obsystem.entities.Milimetro;
import com.obsystem.entities.enums.ProdutoStatus;

public class MilimetroDTO {

    private Integer id;
    private BigDecimal espessura;
    private String descricao;
    private ProdutoStatus status;

    public MilimetroDTO() {
    }

    public MilimetroDTO(Milimetro entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.espessura = entity.getEspessura();
            this.descricao = entity.getDescricao();
            this.status = entity.getStatus();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getEspessura() {
        return espessura;
    }

    public void setEspessura(BigDecimal espessura) {
        this.espessura = espessura;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ProdutoStatus getStatus() {
        return status;
    }

    public void setStatus(ProdutoStatus status) {
        this.status = status;
    }
}

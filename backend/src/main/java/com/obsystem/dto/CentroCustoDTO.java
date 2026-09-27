package com.obsystem.dto;

import com.obsystem.entities.CentroCusto;
import com.obsystem.entities.enums.CentroCustoStatus;
import com.obsystem.entities.enums.TipoCentroCusto;

public class CentroCustoDTO {

    private Integer id;
    private String descricao;
    private TipoCentroCusto tipo;
    private CentroCustoStatus status;

    public CentroCustoDTO() {
    }

    public CentroCustoDTO(CentroCusto entity) {
        this.id = entity.getId();
        this.descricao = entity.getDescricao();
        this.tipo = entity.getTipo();
        this.status = entity.getStatus();
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

    public TipoCentroCusto getTipo() {
        return tipo;
    }

    public void setTipo(TipoCentroCusto tipo) {
        this.tipo = tipo;
    }

    public CentroCustoStatus getStatus() {
        return status;
    }

    public void setStatus(CentroCustoStatus status) {
        this.status = status;
    }
}

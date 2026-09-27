package com.obsystem.dto;

import com.obsystem.entities.PlanoConta;
import com.obsystem.entities.enums.PlanoContaStatus;
import com.obsystem.entities.enums.TipoPlanoConta;

public class PlanoContaDTO {

    private Integer id;
    private String descricao;
    private TipoPlanoConta tipo;
    private PlanoContaStatus status;

    public PlanoContaDTO() {
    }

    public PlanoContaDTO(PlanoConta entity) {
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

    public TipoPlanoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoPlanoConta tipo) {
        this.tipo = tipo;
    }

    public PlanoContaStatus getStatus() {
        return status;
    }

    public void setStatus(PlanoContaStatus status) {
        this.status = status;
    }
}

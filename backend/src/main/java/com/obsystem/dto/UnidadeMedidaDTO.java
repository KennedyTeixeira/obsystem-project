package com.obsystem.dto;

import com.obsystem.entities.UnidadeMedida;
import com.obsystem.entities.enums.ProdutoStatus;

public class UnidadeMedidaDTO {

    private Integer id;
    private String sigla;
    private String descricao;
    private ProdutoStatus status;

    public UnidadeMedidaDTO() {
    }

    public UnidadeMedidaDTO(UnidadeMedida entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.sigla = entity.getSigla();
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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

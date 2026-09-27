package com.obsystem.dto;

import com.obsystem.entities.Categoria;
import com.obsystem.entities.enums.ProdutoStatus;

public class CategoriaDTO {

    private Integer id;
    private String descricao;
    private ProdutoStatus status;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Categoria entity) {
        if (entity != null) {
            this.id = entity.getId();
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

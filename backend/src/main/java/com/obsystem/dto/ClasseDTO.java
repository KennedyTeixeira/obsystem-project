package com.obsystem.dto;

import com.obsystem.entities.Classe;
import com.obsystem.entities.enums.ProdutoStatus;

public class ClasseDTO {

    private Integer id;
    private String descricao;
    private ProdutoStatus status;

    public ClasseDTO() {
    }

    public ClasseDTO(Classe entity) {
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

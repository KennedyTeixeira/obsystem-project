package com.obsystem.dto;

import com.obsystem.entities.SubCategoria;
import com.obsystem.entities.enums.ProdutoStatus;

public class SubCategoriaDTO {

    private Integer id;
    private Integer idCategoria;
    private String nomeCategoria;
    private String descricao;
    private ProdutoStatus status;

    public SubCategoriaDTO() {
    }

    public SubCategoriaDTO(SubCategoria entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.descricao = entity.getDescricao();
            this.status = entity.getStatus();
            if (entity.getCategoria() != null) {
                this.idCategoria = entity.getCategoria().getId();
                this.nomeCategoria = entity.getCategoria().getDescricao();
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
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

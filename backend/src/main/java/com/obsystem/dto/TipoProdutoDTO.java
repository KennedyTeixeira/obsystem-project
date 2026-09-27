package com.obsystem.dto;

import com.obsystem.entities.TipoProduto;
import com.obsystem.entities.enums.ProdutoStatus;

public class TipoProdutoDTO {

    private Integer id;
    private String nome;
    private String descricao;
    private ProdutoStatus status;

    public TipoProdutoDTO() {
    }

    public TipoProdutoDTO(TipoProduto entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.nome = entity.getNome();
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

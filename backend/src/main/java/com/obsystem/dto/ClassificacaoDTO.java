package com.obsystem.dto;

import com.obsystem.entities.Classificacao;
import com.obsystem.entities.enums.ClassificacaoStatus;
import com.obsystem.entities.enums.TipoClassificacao;

public class ClassificacaoDTO {

    private Integer id;
    private String descricao;
    private TipoClassificacao tipo;
    private ClassificacaoStatus status;

    public ClassificacaoDTO() {
    }

    public ClassificacaoDTO(Classificacao entity) {
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

    public TipoClassificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoClassificacao tipo) {
        this.tipo = tipo;
    }

    public ClassificacaoStatus getStatus() {
        return status;
    }

    public void setStatus(ClassificacaoStatus status) {
        this.status = status;
    }
}

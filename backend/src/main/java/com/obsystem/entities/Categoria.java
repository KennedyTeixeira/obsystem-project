package com.obsystem.entities;

import java.util.Objects;

import com.obsystem.entities.enums.ProdutoStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    @Column(name = "ds_categoria", length = 50, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_categoria", length = 20, nullable = false)
    private ProdutoStatus status = ProdutoStatus.ATIVO;

    public Categoria() {
    }

    public Categoria(String descricao, ProdutoStatus status) {
        this.descricao = descricao;
        this.status = status != null ? status : ProdutoStatus.ATIVO;
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria other = (Categoria) obj;
        return Objects.equals(id, other.id);
    }
}

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
@Table(name = "obs_tipo_produto")
public class TipoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_produto")
    private Integer id;

    @Column(name = "nm_tipo_produto", length = 50, nullable = false)
    private String nome;

    @Column(name = "ds_tipo_produto", length = 150)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_tipo_produto", length = 20, nullable = false)
    private ProdutoStatus status = ProdutoStatus.ATIVO;

    public TipoProduto() {
    }

    public TipoProduto(String nome, String descricao, ProdutoStatus status) {
        this.nome = nome;
        this.descricao = descricao;
        this.status = status != null ? status : ProdutoStatus.ATIVO;
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TipoProduto other = (TipoProduto) obj;
        return Objects.equals(id, other.id);
    }
}

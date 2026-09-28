package com.obsystem.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.obsystem.entities.enums.ProdutoStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "obs_sub_categoria")
public class SubCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sub_categoria")
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "obs_categoria_sub_categoria",
        joinColumns = @JoinColumn(name = "id_sub_categoria"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private Set<Categoria> categorias = new HashSet<>();

    @Column(name = "ds_sub_categoria", length = 50, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_sub_categoria", length = 20, nullable = false)
    private ProdutoStatus status = ProdutoStatus.ATIVO;

    public SubCategoria() {
    }

    public SubCategoria(String descricao, ProdutoStatus status) {
        this.descricao = descricao;
        this.status = status != null ? status : ProdutoStatus.ATIVO;
    }

    public SubCategoria(Categoria categoria, String descricao, ProdutoStatus status) {
        this.descricao = descricao;
        this.status = status != null ? status : ProdutoStatus.ATIVO;
        if (categoria != null) {
            this.categorias.add(categoria);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    // Helper compatibility method
    public Categoria getCategoria() {
        return (categorias != null && !categorias.isEmpty()) ? categorias.iterator().next() : null;
    }

    public void setCategoria(Categoria categoria) {
        if (this.categorias == null) {
            this.categorias = new HashSet<>();
        }
        this.categorias.clear();
        if (categoria != null) {
            this.categorias.add(categoria);
        }
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
        SubCategoria other = (SubCategoria) obj;
        return Objects.equals(id, other.id);
    }
}

package com.obsystem.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.obsystem.entities.Categoria;
import com.obsystem.entities.SubCategoria;
import com.obsystem.entities.enums.ProdutoStatus;

public class SubCategoriaDTO {

    private Integer id;
    private Integer idCategoria;
    private List<Integer> idsCategorias = new ArrayList<>();
    private String nomeCategoria;
    private List<String> nomesCategorias = new ArrayList<>();
    private String descricao;
    private ProdutoStatus status;

    public SubCategoriaDTO() {
    }

    public SubCategoriaDTO(SubCategoria entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.descricao = entity.getDescricao();
            this.status = entity.getStatus();
            if (entity.getCategorias() != null && !entity.getCategorias().isEmpty()) {
                this.idsCategorias = entity.getCategorias().stream()
                        .map(Categoria::getId)
                        .filter(id -> id != null)
                        .collect(Collectors.toList());
                this.nomesCategorias = entity.getCategorias().stream()
                        .map(Categoria::getDescricao)
                        .filter(desc -> desc != null)
                        .collect(Collectors.toList());
                if (!this.idsCategorias.isEmpty()) {
                    this.idCategoria = this.idsCategorias.get(0);
                }
                if (!this.nomesCategorias.isEmpty()) {
                    this.nomeCategoria = String.join(", ", this.nomesCategorias);
                }
            } else if (entity.getCategoria() != null) {
                this.idCategoria = entity.getCategoria().getId();
                this.nomeCategoria = entity.getCategoria().getDescricao();
                if (this.idCategoria != null) {
                    this.idsCategorias.add(this.idCategoria);
                }
                if (this.nomeCategoria != null) {
                    this.nomesCategorias.add(this.nomeCategoria);
                }
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
        if (idCategoria != null && !this.idsCategorias.contains(idCategoria)) {
            this.idsCategorias.add(idCategoria);
        }
    }

    public List<Integer> getIdsCategorias() {
        return idsCategorias;
    }

    public void setIdsCategorias(List<Integer> idsCategorias) {
        this.idsCategorias = idsCategorias != null ? idsCategorias : new ArrayList<>();
        if (!this.idsCategorias.isEmpty() && this.idCategoria == null) {
            this.idCategoria = this.idsCategorias.get(0);
        }
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public List<String> getNomesCategorias() {
        return nomesCategorias;
    }

    public void setNomesCategorias(List<String> nomesCategorias) {
        this.nomesCategorias = nomesCategorias != null ? nomesCategorias : new ArrayList<>();
        if (!this.nomesCategorias.isEmpty() && (this.nomeCategoria == null || this.nomeCategoria.isEmpty())) {
            this.nomeCategoria = String.join(", ", this.nomesCategorias);
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
}

package com.obsystem.entities;

import java.math.BigDecimal;
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
@Table(name = "obs_milimetro")
public class Milimetro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_milimetro")
    private Integer id;

    @Column(name = "nr_milimetro", precision = 6, scale = 2, nullable = false)
    private BigDecimal espessura = BigDecimal.ZERO;

    @Column(name = "ds_milimetro", length = 50, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_milimetro", length = 20, nullable = false)
    private ProdutoStatus status = ProdutoStatus.ATIVO;

    public Milimetro() {
    }

    public Milimetro(BigDecimal espessura, String descricao, ProdutoStatus status) {
        this.espessura = espessura;
        this.descricao = descricao;
        this.status = status != null ? status : ProdutoStatus.ATIVO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getEspessura() {
        return espessura;
    }

    public void setEspessura(BigDecimal espessura) {
        this.espessura = espessura;
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
        Milimetro other = (Milimetro) obj;
        return Objects.equals(id, other.id);
    }
}

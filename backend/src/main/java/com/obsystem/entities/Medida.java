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
@Table(name = "obs_medida")
public class Medida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medida")
    private Integer id;

    @Column(name = "ds_medida", length = 50, nullable = false)
    private String descricao;

    @Column(name = "nr_largura", precision = 8, scale = 2)
    private BigDecimal largura;

    @Column(name = "nr_altura", precision = 8, scale = 2)
    private BigDecimal altura;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_medida", length = 20, nullable = false)
    private ProdutoStatus status = ProdutoStatus.ATIVO;

    public Medida() {
    }

    public Medida(String descricao, BigDecimal largura, BigDecimal altura, ProdutoStatus status) {
        this.descricao = descricao;
        this.largura = largura;
        this.altura = altura;
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

    public BigDecimal getLargura() {
        return largura;
    }

    public void setLargura(BigDecimal largura) {
        this.largura = largura;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
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
        Medida other = (Medida) obj;
        return Objects.equals(id, other.id);
    }
}

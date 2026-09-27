package com.obsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.obsystem.entities.Compra;
import com.obsystem.entities.Fisica;
import com.obsystem.entities.Juridica;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.enums.CompraStatus;

public class CompraDTO {

    private Integer id;
    private Integer idFornecedor;
    private String nomeFornecedor;
    private String documentoFornecedor;
    private LocalDate emissao;
    private LocalDate previsao;
    private CompraStatus statusCompra;
    private BigDecimal totalCompra;
    private List<ItemCompraDTO> itens = new ArrayList<>();
    private Boolean gerarTituloFinanceiro = true;

    public CompraDTO() {
    }

    public CompraDTO(Compra entity) {
        this.id = entity.getId();
        if (entity.getFornecedor() != null) {
            this.idFornecedor = entity.getFornecedor().getId();
            Pessoa p = entity.getFornecedor().getPessoa();
            if (p != null) {
                this.nomeFornecedor = p.getNome();
                if (p instanceof Fisica) {
                    this.documentoFornecedor = ((Fisica) p).getCpf();
                } else if (p instanceof Juridica) {
                    this.documentoFornecedor = ((Juridica) p).getCnpj();
                }
            }
        }
        this.emissao = entity.getEmissao();
        this.previsao = entity.getPrevisao();
        this.statusCompra = entity.getStatusCompra();
        this.totalCompra = entity.getTotalCompra();
        if (entity.getItens() != null) {
            this.itens = entity.getItens().stream().map(ItemCompraDTO::new).collect(Collectors.toList());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getDocumentoFornecedor() {
        return documentoFornecedor;
    }

    public void setDocumentoFornecedor(String documentoFornecedor) {
        this.documentoFornecedor = documentoFornecedor;
    }

    public LocalDate getEmissao() {
        return emissao;
    }

    public void setEmissao(LocalDate emissao) {
        this.emissao = emissao;
    }

    public LocalDate getPrevisao() {
        return previsao;
    }

    public void setPrevisao(LocalDate previsao) {
        this.previsao = previsao;
    }

    public CompraStatus getStatusCompra() {
        return statusCompra;
    }

    public void setStatusCompra(CompraStatus statusCompra) {
        this.statusCompra = statusCompra;
    }

    public BigDecimal getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(BigDecimal totalCompra) {
        this.totalCompra = totalCompra;
    }

    public List<ItemCompraDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemCompraDTO> itens) {
        this.itens = itens;
    }

    public Boolean getGerarTituloFinanceiro() {
        return gerarTituloFinanceiro;
    }

    public void setGerarTituloFinanceiro(Boolean gerarTituloFinanceiro) {
        this.gerarTituloFinanceiro = gerarTituloFinanceiro;
    }
}

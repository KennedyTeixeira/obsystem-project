package com.obsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.obsystem.entities.Cliente;
import com.obsystem.entities.Fisica;
import com.obsystem.entities.Juridica;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.Venda;
import com.obsystem.entities.enums.VendaStatus;

public class VendaDTO {

    private Integer id;
    private Integer idCliente;
    private String nomeCliente;
    private String documentoCliente;
    private LocalDate emissao;
    private LocalDate previsao;
    private VendaStatus statusVenda;
    private BigDecimal totalVenda;
    private List<ItemVendaDTO> itens = new ArrayList<>();
    private Boolean gerarTituloFinanceiro = true;

    public VendaDTO() {
    }

    public VendaDTO(Venda entity) {
        this.id = entity.getId();
        this.emissao = entity.getEmissao();
        this.previsao = entity.getPrevisao();
        this.statusVenda = entity.getStatusVenda();
        this.totalVenda = entity.getTotalVenda();

        Cliente c = entity.getCliente();
        if (c != null) {
            this.idCliente = c.getId();
            Pessoa p = c.getPessoa();
            if (p != null) {
                this.nomeCliente = p.getNome();
                if (p instanceof Fisica) {
                    this.documentoCliente = ((Fisica) p).getCpf();
                } else if (p instanceof Juridica) {
                    this.documentoCliente = ((Juridica) p).getCnpj();
                }
            }
        }

        if (entity.getItens() != null) {
            this.itens = entity.getItens().stream().map(ItemVendaDTO::new).collect(Collectors.toList());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
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

    public VendaStatus getStatusVenda() {
        return statusVenda;
    }

    public void setStatusVenda(VendaStatus statusVenda) {
        this.statusVenda = statusVenda;
    }

    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(BigDecimal totalVenda) {
        this.totalVenda = totalVenda;
    }

    public List<ItemVendaDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaDTO> itens) {
        this.itens = itens;
    }

    public Boolean getGerarTituloFinanceiro() {
        return gerarTituloFinanceiro;
    }

    public void setGerarTituloFinanceiro(Boolean gerarTituloFinanceiro) {
        this.gerarTituloFinanceiro = gerarTituloFinanceiro;
    }
}

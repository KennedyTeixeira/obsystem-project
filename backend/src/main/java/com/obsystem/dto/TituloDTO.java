package com.obsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.obsystem.entities.Fisica;
import com.obsystem.entities.Juridica;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.Titulo;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;

public class TituloDTO {

    private Integer id;
    private TipoTitulo tipo;
    private String descricao;
    private Integer idPessoa;
    private String nomePessoa;
    private String documentoPessoa;
    private Integer idVenda;
    private Integer idCompra;
    private Integer idClassificacao;
    private String nomeClassificacao;
    private Integer idPlanoConta;
    private String nomePlanoConta;
    private Integer idCentroCusto;
    private String nomeCentroCusto;
    private LocalDate emissao;
    private LocalDate vencimento;
    private LocalDate pagamento;
    private BigDecimal valorTotal;
    private TituloStatus status;
    private Boolean vencido;

    public TituloDTO() {
    }

    public TituloDTO(Titulo entity) {
        this.id = entity.getId();
        this.tipo = entity.getTipo();
        this.descricao = entity.getDescricao();

        Pessoa p = entity.getPessoa();
        if (p != null) {
            this.idPessoa = p.getId();
            this.nomePessoa = p.getNome();
            if (p instanceof Fisica) {
                this.documentoPessoa = ((Fisica) p).getCpf();
            } else if (p instanceof Juridica) {
                this.documentoPessoa = ((Juridica) p).getCnpj();
            }
        }

        if (entity.getVenda() != null) {
            this.idVenda = entity.getVenda().getId();
        }

        if (entity.getCompra() != null) {
            this.idCompra = entity.getCompra().getId();
        }

        if (entity.getClassificacao() != null) {
            this.idClassificacao = entity.getClassificacao().getId();
            this.nomeClassificacao = entity.getClassificacao().getDescricao();
        }

        if (entity.getPlanoConta() != null) {
            this.idPlanoConta = entity.getPlanoConta().getId();
            this.nomePlanoConta = entity.getPlanoConta().getDescricao();
        }

        if (entity.getCentroCusto() != null) {
            this.idCentroCusto = entity.getCentroCusto().getId();
            this.nomeCentroCusto = entity.getCentroCusto().getDescricao();
        }

        this.emissao = entity.getEmissao();
        this.vencimento = entity.getVencimento();
        this.pagamento = entity.getPagamento();
        this.valorTotal = entity.getValorTotal();
        this.status = entity.getStatus();

        if (entity.getStatus() == TituloStatus.PENDENTE && entity.getVencimento() != null) {
            this.vencido = LocalDate.now().isAfter(entity.getVencimento());
        } else {
            this.vencido = false;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoTitulo getTipo() {
        return tipo;
    }

    public void setTipo(TipoTitulo tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getDocumentoPessoa() {
        return documentoPessoa;
    }

    public void setDocumentoPessoa(String documentoPessoa) {
        this.documentoPessoa = documentoPessoa;
    }

    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        this.idVenda = idVenda;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdClassificacao() {
        return idClassificacao;
    }

    public void setIdClassificacao(Integer idClassificacao) {
        this.idClassificacao = idClassificacao;
    }

    public String getNomeClassificacao() {
        return nomeClassificacao;
    }

    public void setNomeClassificacao(String nomeClassificacao) {
        this.nomeClassificacao = nomeClassificacao;
    }

    public Integer getIdPlanoConta() {
        return idPlanoConta;
    }

    public void setIdPlanoConta(Integer idPlanoConta) {
        this.idPlanoConta = idPlanoConta;
    }

    public String getNomePlanoConta() {
        return nomePlanoConta;
    }

    public void setNomePlanoConta(String nomePlanoConta) {
        this.nomePlanoConta = nomePlanoConta;
    }

    public Integer getIdCentroCusto() {
        return idCentroCusto;
    }

    public void setIdCentroCusto(Integer idCentroCusto) {
        this.idCentroCusto = idCentroCusto;
    }

    public String getNomeCentroCusto() {
        return nomeCentroCusto;
    }

    public void setNomeCentroCusto(String nomeCentroCusto) {
        this.nomeCentroCusto = nomeCentroCusto;
    }

    public LocalDate getEmissao() {
        return emissao;
    }

    public void setEmissao(LocalDate emissao) {
        this.emissao = emissao;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public LocalDate getPagamento() {
        return pagamento;
    }

    public void setPagamento(LocalDate pagamento) {
        this.pagamento = pagamento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public TituloStatus getStatus() {
        return status;
    }

    public void setStatus(TituloStatus status) {
        this.status = status;
    }

    public Boolean getVencido() {
        return vencido;
    }

    public void setVencido(Boolean vencido) {
        this.vencido = vencido;
    }
}

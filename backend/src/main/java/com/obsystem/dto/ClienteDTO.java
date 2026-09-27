package com.obsystem.dto;

import java.math.BigDecimal;

import com.obsystem.entities.Cliente;
import com.obsystem.entities.Fisica;
import com.obsystem.entities.Juridica;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.enums.ClienteStatus;
import com.obsystem.entities.enums.Genero;
import com.obsystem.entities.enums.TipoContato;

public class ClienteDTO {

    private Integer id;
    private Integer idPessoa;
    private String nome;
    private Character tipoPessoa; // 'F' ou 'J'
    private String email;
    private String segmento;
    private String site;
    private String observacao;

    // Pessoa Física
    private String cpf;
    private String rg;
    private Genero genero;

    // Pessoa Jurídica
    private String cnpj;
    private String fantasia;
    private String ie;
    private String im;

    // Dados específicos do Cliente
    private ClienteStatus status;
    private BigDecimal saldo;
    private BigDecimal limite;

    // Endereço rápido
    private String cep;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    // Contato rápido
    private String telefone;
    private TipoContato tipoContato;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.status = cliente.getStatus();
        this.saldo = cliente.getSaldo();
        this.limite = cliente.getLimite();

        Pessoa p = cliente.getPessoa();
        if (p != null) {
            this.idPessoa = p.getId();
            this.nome = p.getNome();
            this.tipoPessoa = p.getTipo();
            this.email = p.getEmail();
            this.segmento = p.getSegmento();
            this.site = p.getSite();
            this.observacao = p.getObservacao();

            if (p instanceof Fisica) {
                Fisica f = (Fisica) p;
                this.cpf = f.getCpf();
                this.rg = f.getRg();
                this.genero = f.getGenero();
            } else if (p instanceof Juridica) {
                Juridica j = (Juridica) p;
                this.cnpj = j.getCnpj();
                this.fantasia = j.getFantasia();
                this.ie = j.getIe();
                this.im = j.getIm();
            }
        }
    }

    public String getDocumento() {
        if (this.tipoPessoa != null && this.tipoPessoa == 'F') {
            return this.cpf;
        }
        return this.cnpj;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Character getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(Character tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public ClienteStatus getStatus() {
        return status;
    }

    public void setStatus(ClienteStatus status) {
        this.status = status;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoContato getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(TipoContato tipoContato) {
        this.tipoContato = tipoContato;
    }
}

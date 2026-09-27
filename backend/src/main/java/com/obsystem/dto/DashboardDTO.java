package com.obsystem.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashboardDTO {

    // Vendas
    private BigDecimal totalVendasMes = BigDecimal.ZERO;
    private Long qtdVendasMes = 0L;

    // Compras
    private BigDecimal totalComprasMes = BigDecimal.ZERO;
    private Long qtdComprasMes = 0L;

    // Financeiro
    private BigDecimal totalReceberPendente = BigDecimal.ZERO;
    private BigDecimal totalRecebidoMes = BigDecimal.ZERO;
    private BigDecimal totalPagarPendente = BigDecimal.ZERO;
    private BigDecimal totalPagoMes = BigDecimal.ZERO;
    private BigDecimal saldoPrevisto = BigDecimal.ZERO;
    private BigDecimal totalVencidos = BigDecimal.ZERO;
    private Long qtdVencidos = 0L;

    // Alertas Operacionais
    private Long qtdProdutosEstoqueBaixo = 0L;
    private Long qtdClientesAtivos = 0L;
    private Long qtdFornecedoresAtivos = 0L;

    // Listas Recentes
    private List<VendaDTO> ultimasVendas = new ArrayList<>();
    private List<CompraDTO> ultimasCompras = new ArrayList<>();
    private List<TituloDTO> proximosTitulos = new ArrayList<>();

    public DashboardDTO() {
    }

    public BigDecimal getTotalVendasMes() {
        return totalVendasMes;
    }

    public void setTotalVendasMes(BigDecimal totalVendasMes) {
        this.totalVendasMes = totalVendasMes;
    }

    public Long getQtdVendasMes() {
        return qtdVendasMes;
    }

    public void setQtdVendasMes(Long qtdVendasMes) {
        this.qtdVendasMes = qtdVendasMes;
    }

    public BigDecimal getTotalComprasMes() {
        return totalComprasMes;
    }

    public void setTotalComprasMes(BigDecimal totalComprasMes) {
        this.totalComprasMes = totalComprasMes;
    }

    public Long getQtdComprasMes() {
        return qtdComprasMes;
    }

    public void setQtdComprasMes(Long qtdComprasMes) {
        this.qtdComprasMes = qtdComprasMes;
    }

    public BigDecimal getTotalReceberPendente() {
        return totalReceberPendente;
    }

    public void setTotalReceberPendente(BigDecimal totalReceberPendente) {
        this.totalReceberPendente = totalReceberPendente;
    }

    public BigDecimal getTotalRecebidoMes() {
        return totalRecebidoMes;
    }

    public void setTotalRecebidoMes(BigDecimal totalRecebidoMes) {
        this.totalRecebidoMes = totalRecebidoMes;
    }

    public BigDecimal getTotalPagarPendente() {
        return totalPagarPendente;
    }

    public void setTotalPagarPendente(BigDecimal totalPagarPendente) {
        this.totalPagarPendente = totalPagarPendente;
    }

    public BigDecimal getTotalPagoMes() {
        return totalPagoMes;
    }

    public void setTotalPagoMes(BigDecimal totalPagoMes) {
        this.totalPagoMes = totalPagoMes;
    }

    public BigDecimal getSaldoPrevisto() {
        return saldoPrevisto;
    }

    public void setSaldoPrevisto(BigDecimal saldoPrevisto) {
        this.saldoPrevisto = saldoPrevisto;
    }

    public BigDecimal getTotalVencidos() {
        return totalVencidos;
    }

    public void setTotalVencidos(BigDecimal totalVencidos) {
        this.totalVencidos = totalVencidos;
    }

    public Long getQtdVencidos() {
        return qtdVencidos;
    }

    public void setQtdVencidos(Long qtdVencidos) {
        this.qtdVencidos = qtdVencidos;
    }

    public Long getQtdProdutosEstoqueBaixo() {
        return qtdProdutosEstoqueBaixo;
    }

    public void setQtdProdutosEstoqueBaixo(Long qtdProdutosEstoqueBaixo) {
        this.qtdProdutosEstoqueBaixo = qtdProdutosEstoqueBaixo;
    }

    public Long getQtdClientesAtivos() {
        return qtdClientesAtivos;
    }

    public void setQtdClientesAtivos(Long qtdClientesAtivos) {
        this.qtdClientesAtivos = qtdClientesAtivos;
    }

    public Long getQtdFornecedoresAtivos() {
        return qtdFornecedoresAtivos;
    }

    public void setQtdFornecedoresAtivos(Long qtdFornecedoresAtivos) {
        this.qtdFornecedoresAtivos = qtdFornecedoresAtivos;
    }

    public List<VendaDTO> getUltimasVendas() {
        return ultimasVendas;
    }

    public void setUltimasVendas(List<VendaDTO> ultimasVendas) {
        this.ultimasVendas = ultimasVendas;
    }

    public List<CompraDTO> getUltimasCompras() {
        return ultimasCompras;
    }

    public void setUltimasCompras(List<CompraDTO> ultimasCompras) {
        this.ultimasCompras = ultimasCompras;
    }

    public List<TituloDTO> getProximosTitulos() {
        return proximosTitulos;
    }

    public void setProximosTitulos(List<TituloDTO> proximosTitulos) {
        this.proximosTitulos = proximosTitulos;
    }
}

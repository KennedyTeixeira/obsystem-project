package com.obsystem.dto;

import java.math.BigDecimal;

public class ResumoFinanceiroDTO {

    private BigDecimal totalReceberPendente = BigDecimal.ZERO;
    private BigDecimal totalPagarPendente = BigDecimal.ZERO;
    private BigDecimal saldoPrevisto = BigDecimal.ZERO;
    private BigDecimal totalRecebidoMes = BigDecimal.ZERO;
    private BigDecimal totalPagoMes = BigDecimal.ZERO;
    private BigDecimal totalVencido = BigDecimal.ZERO;

    public ResumoFinanceiroDTO() {
    }

    public ResumoFinanceiroDTO(BigDecimal totalReceberPendente, BigDecimal totalPagarPendente,
                               BigDecimal totalRecebidoMes, BigDecimal totalPagoMes, BigDecimal totalVencido) {
        this.totalReceberPendente = totalReceberPendente != null ? totalReceberPendente : BigDecimal.ZERO;
        this.totalPagarPendente = totalPagarPendente != null ? totalPagarPendente : BigDecimal.ZERO;
        this.saldoPrevisto = this.totalReceberPendente.subtract(this.totalPagarPendente);
        this.totalRecebidoMes = totalRecebidoMes != null ? totalRecebidoMes : BigDecimal.ZERO;
        this.totalPagoMes = totalPagoMes != null ? totalPagoMes : BigDecimal.ZERO;
        this.totalVencido = totalVencido != null ? totalVencido : BigDecimal.ZERO;
    }

    public BigDecimal getTotalReceberPendente() {
        return totalReceberPendente;
    }

    public void setTotalReceberPendente(BigDecimal totalReceberPendente) {
        this.totalReceberPendente = totalReceberPendente;
    }

    public BigDecimal getTotalPagarPendente() {
        return totalPagarPendente;
    }

    public void setTotalPagarPendente(BigDecimal totalPagarPendente) {
        this.totalPagarPendente = totalPagarPendente;
    }

    public BigDecimal getSaldoPrevisto() {
        return saldoPrevisto;
    }

    public void setSaldoPrevisto(BigDecimal saldoPrevisto) {
        this.saldoPrevisto = saldoPrevisto;
    }

    public BigDecimal getTotalRecebidoMes() {
        return totalRecebidoMes;
    }

    public void setTotalRecebidoMes(BigDecimal totalRecebidoMes) {
        this.totalRecebidoMes = totalRecebidoMes;
    }

    public BigDecimal getTotalPagoMes() {
        return totalPagoMes;
    }

    public void setTotalPagoMes(BigDecimal totalPagoMes) {
        this.totalPagoMes = totalPagoMes;
    }

    public BigDecimal getTotalVencido() {
        return totalVencido;
    }

    public void setTotalVencido(BigDecimal totalVencido) {
        this.totalVencido = totalVencido;
    }
}

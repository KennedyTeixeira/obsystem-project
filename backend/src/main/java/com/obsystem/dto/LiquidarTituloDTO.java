package com.obsystem.dto;

import java.time.LocalDate;

public class LiquidarTituloDTO {

    private LocalDate dataPagamento;

    public LiquidarTituloDTO() {
    }

    public LiquidarTituloDTO(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}

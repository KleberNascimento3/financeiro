package com.kleber.financeiro.dto;

import java.math.BigDecimal;

public class EventoResumoDTO {

    private String evento;
    private BigDecimal receitas;
    private BigDecimal despesas;

    public EventoResumoDTO(
            String evento,
            BigDecimal receitas,
            BigDecimal despesas) {

        this.evento = evento;
        this.receitas = receitas;
        this.despesas = despesas;
    }

    public String getEvento() {
        return evento;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public BigDecimal getSaldo() {
        return receitas.subtract(despesas);
    }
}
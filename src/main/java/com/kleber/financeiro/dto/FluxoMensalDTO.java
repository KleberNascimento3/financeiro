package com.kleber.financeiro.dto;

import java.math.BigDecimal;

public class FluxoMensalDTO {

    private String mes;
    private BigDecimal receitas;
    private BigDecimal despesas;

    public FluxoMensalDTO(
            String mes,
            BigDecimal receitas,
            BigDecimal despesas) {

        this.mes = mes;
        this.receitas = receitas;
        this.despesas = despesas;
    }

    public String getMes() {
        return mes;
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
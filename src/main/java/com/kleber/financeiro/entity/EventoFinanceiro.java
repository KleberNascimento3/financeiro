package com.kleber.financeiro.entity;

import java.time.LocalDate;

import com.kleber.financeiro.enums.TipoEventoFinanceiro;

import jakarta.persistence.*;

@Entity
@Table(name = "eventos_financeiros")
public class EventoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer indice;

    private String descricao;

    private LocalDate dataEvento;

    @Enumerated(EnumType.STRING)
    private TipoEventoFinanceiro tipo;

    @Column(nullable = false)
    private Boolean encerrado = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public TipoEventoFinanceiro getTipo() {
        return tipo;
    }

    public void setTipo(TipoEventoFinanceiro tipo) {
        this.tipo = tipo;
    }

    public Boolean getEncerrado() {
        return encerrado;
    }

    public void setEncerrado(Boolean encerrado) {
        this.encerrado = encerrado;
    }
}
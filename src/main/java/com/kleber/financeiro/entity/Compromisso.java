package com.kleber.financeiro.entity;

import java.math.BigDecimal;

import com.kleber.financeiro.enums.TipoCompromisso;

import jakarta.persistence.*;

@Entity
@Table(name = "compromissos")
public class Compromisso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private TipoCompromisso tipo;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoCompromisso getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompromisso tipo) {
		this.tipo = tipo;
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Integer getParcelasRestantes() {
		return parcelasRestantes;
	}

	public void setParcelasRestantes(Integer parcelasRestantes) {
		this.parcelasRestantes = parcelasRestantes;
	}

	public Integer getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(Integer diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	private Integer quantidadeParcelas;

    private Integer parcelasRestantes;

    private Integer diaVencimento;

    private Boolean ativo = true;

    // getters e setters

}
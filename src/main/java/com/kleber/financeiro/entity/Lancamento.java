package com.kleber.financeiro.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.kleber.financeiro.enums.StatusLancamento;
import com.kleber.financeiro.enums.TipoLancamento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe a descrição")
    private String descricao;

    @NotNull(message = "Selecione uma categoria")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @NotNull(message = "Informe o valor")
    private BigDecimal valor;

    @NotNull(message = "Informe a data de vencimento")
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe o tipo")
    private TipoLancamento tipo;    

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe o status")
    private StatusLancamento status;   

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private EventoFinanceiro evento;
    
    @ManyToOne
    @JoinColumn(name = "compromisso_id")
    private Compromisso compromisso;
    
    private Boolean parcelado;

    private Integer totalParcelas;

    private Integer numeroParcela;
    
    private Boolean recorrente = false;
    
    private Integer diaRecorrencia;
    
    public Integer getDiaRecorrencia() {
		return diaRecorrencia;
	}

	public void setDiaRecorrencia(Integer diaRecorrencia) {
		this.diaRecorrencia = diaRecorrencia;
	}

	@ManyToOne
	@JoinColumn(name = "cartao_id", nullable = true)
	private Cartao cartao;
	
    // GETTERS E SETTERS

    public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public Boolean getRecorrente() {
		return recorrente;
	}

	public void setRecorrente(Boolean recorrente) {
		this.recorrente = recorrente;
	}

	public Boolean getParcelado() {
		return parcelado;
	}

	public void setParcelado(Boolean parcelado) {
		this.parcelado = parcelado;
	}

	public Integer getTotalParcelas() {
		return totalParcelas;
	}

	public void setTotalParcelas(Integer totalParcelas) {
		this.totalParcelas = totalParcelas;
	}

	public Integer getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Compromisso getCompromisso() {
		return compromisso;
	}

	public void setCompromisso(Compromisso compromisso) {
		this.compromisso = compromisso;
	}

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

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoLancamento tipo) {
        this.tipo = tipo;
    }

    public StatusLancamento getStatus() {
        return status;
    }

    public void setStatus(StatusLancamento status) {
        this.status = status;
    }

    public EventoFinanceiro getEvento() {
        return evento;
    }

    public void setEvento(EventoFinanceiro evento) {
        this.evento = evento;
    }
}
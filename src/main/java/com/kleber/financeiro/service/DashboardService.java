package com.kleber.financeiro.service;


import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kleber.financeiro.dto.FluxoMensalDTO;
import com.kleber.financeiro.entity.Lancamento;
import com.kleber.financeiro.enums.TipoLancamento;
import com.kleber.financeiro.repository.LancamentoRepository;
import java.util.HashMap;

import com.kleber.financeiro.dto.EventoResumoDTO;

@Service
public class DashboardService {

    private final LancamentoRepository repository;

    public DashboardService(LancamentoRepository repository) {
        this.repository = repository;
    }

    public BigDecimal totalReceitas() {

        return repository.findAll()
                .stream()
                .filter(l -> l.getTipo() == TipoLancamento.RECEITA)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalDespesas() {

        return repository.findAll()
                .stream()
                .filter(l -> l.getTipo() == TipoLancamento.DESPESA)
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal saldo() {
        return totalReceitas().subtract(totalDespesas());
    }
    
    public List<FluxoMensalDTO> fluxoMensal() {

        Map<YearMonth, List<Lancamento>> agrupado =
                repository.findAll()
                .stream()
                .filter(l -> l.getDataVencimento() != null)
                .collect(Collectors.groupingBy(
                        l -> YearMonth.from(
                                l.getDataVencimento())
                ));

        List<FluxoMensalDTO> lista =
                new ArrayList<>();

        agrupado.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {

                    BigDecimal receitas =
                            entry.getValue()
                            .stream()
                            .filter(l -> l.getTipo() == TipoLancamento.RECEITA)
                            .map(Lancamento::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal despesas =
                            entry.getValue()
                            .stream()
                            .filter(l -> l.getTipo() == TipoLancamento.DESPESA)
                            .map(Lancamento::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    lista.add(
                            new FluxoMensalDTO(
                                    entry.getKey().toString(),
                                    receitas,
                                    despesas));
                });

        return lista;
    }
    
    public List<EventoResumoDTO> resumoPorEvento() {

        Map<String, EventoResumoDTO> mapa =
                new HashMap<>();

        repository.findAll().forEach(l -> {

            String nomeEvento =
                    l.getEvento() != null
                    ? l.getEvento().getDescricao()
                    : "Sem Evento";

            EventoResumoDTO dto =
                    mapa.getOrDefault(
                            nomeEvento,
                            new EventoResumoDTO(
                                    nomeEvento,
                                    BigDecimal.ZERO,
                                    BigDecimal.ZERO));

            BigDecimal receitas =
                    dto.getReceitas();

            BigDecimal despesas =
                    dto.getDespesas();

            if (l.getTipo() == TipoLancamento.RECEITA) {
                receitas = receitas.add(l.getValor());
            } else {
                despesas = despesas.add(l.getValor());
            }

            mapa.put(
                    nomeEvento,
                    new EventoResumoDTO(
                            nomeEvento,
                            receitas,
                            despesas));
        });

        return new ArrayList<>(mapa.values());
    }
    
}

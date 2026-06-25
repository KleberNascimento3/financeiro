package com.kleber.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kleber.financeiro.dto.EventoResumoDTO;
import com.kleber.financeiro.dto.FluxoMensalDTO;
import com.kleber.financeiro.entity.Lancamento;
import com.kleber.financeiro.enums.TipoLancamento;
import com.kleber.financeiro.repository.LancamentoRepository;

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

        Map<String, EventoResumoAgrupado> mapa =
                new HashMap<>();

        repository.findAll().forEach(l -> {

            String nomeEvento =
                    l.getEvento() != null
                    ? l.getEvento().getDescricao()
                    : "Sem Evento";

            Integer indiceEvento =
                    l.getEvento() != null && l.getEvento().getIndice() != null
                    ? l.getEvento().getIndice()
                    : Integer.MAX_VALUE;

            LocalDate dataEvento =
                    l.getEvento() != null && l.getEvento().getDataEvento() != null
                    ? l.getEvento().getDataEvento()
                    : LocalDate.MAX;

            EventoResumoAgrupado resumo =
                    mapa.getOrDefault(
                            nomeEvento,
                            new EventoResumoAgrupado(
                                    nomeEvento,
                                    BigDecimal.ZERO,
                                    BigDecimal.ZERO,
                                    indiceEvento,
                                    dataEvento));

            BigDecimal receitas = resumo.receitas;
            BigDecimal despesas = resumo.despesas;

            if (l.getValor() != null && l.getTipo() == TipoLancamento.RECEITA) {
                receitas = receitas.add(l.getValor());
            } else if (l.getValor() != null) {
                despesas = despesas.add(l.getValor());
            }

            mapa.put(
                    nomeEvento,
                    new EventoResumoAgrupado(
                            nomeEvento,
                            receitas,
                            despesas,
                            Math.min(resumo.indiceEvento, indiceEvento),
                            resumo.dataEvento.isBefore(dataEvento)
                                    ? resumo.dataEvento
                                    : dataEvento));
        });

        return mapa.values()
                .stream()
                .sorted(Comparator
                        .comparing((EventoResumoAgrupado r) -> r.indiceEvento)
                        .thenComparing(r -> r.dataEvento)
                        .thenComparing(r -> r.evento))
                .map(r -> new EventoResumoDTO(
                        r.evento,
                        r.receitas,
                        r.despesas))
                .collect(Collectors.toList());
    }

    private static class EventoResumoAgrupado {

        private final String evento;
        private final BigDecimal receitas;
        private final BigDecimal despesas;
        private final Integer indiceEvento;
        private final LocalDate dataEvento;

        private EventoResumoAgrupado(
                String evento,
                BigDecimal receitas,
                BigDecimal despesas,
                Integer indiceEvento,
                LocalDate dataEvento) {

            this.evento = evento;
            this.receitas = receitas;
            this.despesas = despesas;
            this.indiceEvento = indiceEvento;
            this.dataEvento = dataEvento;
        }
    }
}

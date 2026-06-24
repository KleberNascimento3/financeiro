package com.kleber.financeiro.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.kleber.financeiro.entity.EventoFinanceiro;
import com.kleber.financeiro.entity.Lancamento;
import com.kleber.financeiro.enums.TipoEventoFinanceiro;
import com.kleber.financeiro.enums.TipoLancamento;
import com.kleber.financeiro.repository.EventoFinanceiroRepository;
import com.kleber.financeiro.repository.LancamentoRepository;

@Service
public class EventoAutomaticoService {

    private final EventoFinanceiroRepository eventoRepository;
    private final LancamentoRepository lancamentoRepository;

    public EventoAutomaticoService(
            EventoFinanceiroRepository eventoRepository,
            LancamentoRepository lancamentoRepository) {

        this.eventoRepository = eventoRepository;
        this.lancamentoRepository = lancamentoRepository;
    }

    public EventoFinanceiro obterOuCriarEvento(LocalDate dataVencimento) {

        LocalDate dataEvento = definirDataEvento(dataVencimento);
        String descricao = definirDescricaoEvento(dataEvento);

        return eventoRepository
                .findByDescricaoAndDataEvento(descricao, dataEvento)
                .orElseGet(() -> {

                    EventoFinanceiro evento = new EventoFinanceiro();

                    evento.setDescricao(descricao);
                    evento.setDataEvento(dataEvento);
                    evento.setIndice(proximoIndice());
                    evento.setEncerrado(false);

                    if (descricao.startsWith("Vale")) {
                        evento.setTipo(TipoEventoFinanceiro.VALE);
                    } else {
                        evento.setTipo(TipoEventoFinanceiro.PAGAMENTO);
                    }

                    EventoFinanceiro salvo =
                            eventoRepository.save(evento);

                    gerarRecorrentes(salvo);

                    return salvo;
                });
    }

    private LocalDate definirDataEvento(LocalDate data) {

        int dia = data.getDayOfMonth();

        if (dia >= 15 && dia <= 29) {
            return LocalDate.of(
                    data.getYear(),
                    data.getMonth(),
                    15);
        }

        int ultimoDiaMes = data.lengthOfMonth();
        int diaPagamento = Math.min(30, ultimoDiaMes);

        return LocalDate.of(
                data.getYear(),
                data.getMonth(),
                diaPagamento);
    }

    private String definirDescricaoEvento(LocalDate dataEvento) {

        String mes =
                nomeMes(dataEvento.getMonth());

        if (dataEvento.getDayOfMonth() == 15) {
            return "Vale " + mes + " " + dataEvento.getYear();
        }

        return "Pagamento " + mes + " " + dataEvento.getYear();
    }

    private String nomeMes(Month mes) {
        return mes.getDisplayName(
                TextStyle.FULL,
                new Locale("pt", "BR"));
    }

    private Integer proximoIndice() {

        EventoFinanceiro ultimo =
                eventoRepository.findTopByOrderByIndiceDesc();

        if (ultimo == null || ultimo.getIndice() == null) {
            return 1;
        }

        return ultimo.getIndice() + 1;
    }

    private void gerarRecorrentes(EventoFinanceiro evento) {

        Integer diaRecorrencia =
                evento.getDataEvento().getDayOfMonth() == 15
                        ? 15
                        : 30;

        List<Lancamento> recorrentes =
                new ArrayList<>(lancamentoRepository
                        .findByRecorrenteTrueAndDiaRecorrencia(
                                diaRecorrencia));

        if (evento.getTipo() == TipoEventoFinanceiro.PAGAMENTO) {
            List<Lancamento> receitasRecorrentes =
                    lancamentoRepository.findByRecorrenteTrueAndTipo(
                            TipoLancamento.RECEITA);

            for (Lancamento receita : receitasRecorrentes) {
                boolean jaEstaNaLista =
                        recorrentes.stream()
                                .anyMatch(r -> r.getId() != null
                                        && r.getId().equals(receita.getId()));

                if (!jaEstaNaLista) {
                    recorrentes.add(receita);
                }
            }
        }

        for (Lancamento r : recorrentes) {

            Lancamento novo = new Lancamento();

            novo.setDescricao(r.getDescricao());
            novo.setValor(r.getValor());
            novo.setTipo(r.getTipo());
            novo.setStatus(r.getStatus());
            novo.setCategoria(r.getCategoria());
            novo.setCartao(r.getCartao());
            novo.setCompromisso(r.getCompromisso());

            novo.setEvento(evento);
            novo.setDataVencimento(evento.getDataEvento());

            novo.setRecorrente(false);
            novo.setParcelado(false);
            novo.setDiaRecorrencia(r.getDiaRecorrencia());

            lancamentoRepository.save(novo);
        }
    }
}

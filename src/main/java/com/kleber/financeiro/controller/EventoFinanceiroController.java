package com.kleber.financeiro.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kleber.financeiro.entity.EventoFinanceiro;
import com.kleber.financeiro.entity.Lancamento;
import com.kleber.financeiro.enums.TipoLancamento;
import com.kleber.financeiro.repository.EventoFinanceiroRepository;
import com.kleber.financeiro.repository.LancamentoRepository;

@Controller
public class EventoFinanceiroController {

    private final EventoFinanceiroRepository repository;
    private final LancamentoRepository lancamentoRepository;

    public EventoFinanceiroController(
            EventoFinanceiroRepository repository,
            LancamentoRepository lancamentoRepository) {

        this.repository = repository;
        this.lancamentoRepository = lancamentoRepository;
    }

    @GetMapping("/eventos")
    public String listar(Model model) {

        model.addAttribute("lista", repository.findAll());

        return "eventos";
    }

    @GetMapping("/eventos/novo")
    public String novo(Model model) {

        model.addAttribute("evento", new EventoFinanceiro());

        return "evento-form";
    }

    @PostMapping("/eventos/salvar")
    public String salvar(@ModelAttribute EventoFinanceiro evento) {

        boolean novoEvento = evento.getId() == null;

        EventoFinanceiro eventoSalvo = repository.save(evento);

        if (novoEvento) {
            gerarLancamentosRecorrentes(eventoSalvo);
        }

        return "redirect:/eventos";
    }

    @GetMapping("/eventos/gerar-recorrentes/{id}")
    public String gerarRecorrentesEventoExistente(
            @PathVariable Long id) {

        EventoFinanceiro evento =
                repository.findById(id).orElseThrow();

        gerarLancamentosRecorrentes(evento);

        return "redirect:/eventos/" + id;
    }

    @GetMapping("/eventos/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        EventoFinanceiro evento =
                repository.findById(id).orElseThrow();

        model.addAttribute("evento", evento);

        return "evento-form";
    }

    @GetMapping("/eventos/encerrar/{id}")
    public String encerrar(@PathVariable Long id) {

        EventoFinanceiro evento =
                repository.findById(id).orElseThrow();

        evento.setEncerrado(true);

        repository.save(evento);

        return "redirect:/eventos";
    }

    @GetMapping("/eventos/{id}")
    public String detalhe(
            @PathVariable Long id,
            Model model) {

        EventoFinanceiro evento =
                repository.findById(id).orElseThrow();

        List<Lancamento> lancamentos =
                lancamentoRepository.findByEventoId(id);

        BigDecimal receitas = BigDecimal.ZERO;
        BigDecimal despesas = BigDecimal.ZERO;

        for (Lancamento l : lancamentos) {

            if (l.getValor() == null) {
                continue;
            }

            if (l.getTipo() == TipoLancamento.RECEITA) {
                receitas = receitas.add(l.getValor());
            } else {
                despesas = despesas.add(l.getValor());
            }
        }

        BigDecimal saldo = receitas.subtract(despesas);

        model.addAttribute("evento", evento);
        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("receitas", receitas);
        model.addAttribute("despesas", despesas);
        model.addAttribute("saldo", saldo);

        return "evento-detalhe";
    }

    private void gerarLancamentosRecorrentes(EventoFinanceiro evento) {

        Integer diaRecorrencia = definirDiaRecorrencia(evento);

        if (diaRecorrencia == null) {
            return;
        }

        List<Lancamento> recorrentes =
                lancamentoRepository
                        .findByRecorrenteTrueAndDiaRecorrencia(diaRecorrencia);

        List<Lancamento> jaLancados =
                lancamentoRepository.findByEventoId(evento.getId());

        for (Lancamento r : recorrentes) {

            boolean jaExiste =
                    jaLancados.stream()
                            .anyMatch(l ->
                                    l.getDescricao() != null
                                    && r.getDescricao() != null
                                    && l.getDescricao().equals(r.getDescricao()));

            if (jaExiste) {
                continue;
            }

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

    private Integer definirDiaRecorrencia(EventoFinanceiro evento) {

        if (evento.getIndice() == null) {
            return null;
        }

        if (evento.getIndice() % 2 == 0) {
            return 30;
        }

        return 15;
    }
}
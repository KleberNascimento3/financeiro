package com.kleber.financeiro.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kleber.financeiro.entity.Categoria;
import com.kleber.financeiro.entity.EventoFinanceiro;
import com.kleber.financeiro.entity.Lancamento;
import com.kleber.financeiro.enums.TipoEventoFinanceiro;
import com.kleber.financeiro.enums.TipoLancamento;
import com.kleber.financeiro.repository.CartaoRepository;
import com.kleber.financeiro.repository.CategoriaRepository;
import com.kleber.financeiro.repository.EventoFinanceiroRepository;
import com.kleber.financeiro.repository.LancamentoRepository;
import com.kleber.financeiro.service.EventoAutomaticoService;


import jakarta.validation.Valid;

@Controller
public class LancamentoController {

    private final LancamentoRepository repository;
    private final EventoFinanceiroRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CartaoRepository cartaoRepository;
	private EventoAutomaticoService eventoAutomaticoService;
    

    public LancamentoController(
            LancamentoRepository repository,
            EventoFinanceiroRepository eventoRepository,
            CategoriaRepository categoriaRepository,
            CartaoRepository cartaoRepository,
            EventoAutomaticoService eventoAutomaticoService) {

        this.repository = repository;
        this.eventoRepository = eventoRepository;
        this.categoriaRepository = categoriaRepository;
        this.cartaoRepository = cartaoRepository;
        this.eventoAutomaticoService = eventoAutomaticoService;
    }

    @GetMapping("/lancamentos")
    public String listar(
            @RequestParam(required = false) Long eventoId,
            @RequestParam(required = false) Long cartaoId,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim,
            Model model) {
    	
    	List<Lancamento> lista;

    	if (eventoId != null && inicio != null && fim != null) {
    		
    		

    	    lista = repository
    	            .findByEventoIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(
    	                    eventoId,
    	                    inicio,
    	                    fim);

    	} else if (eventoId != null) {

    	    lista = repository
    	            .findByEventoIdOrderByDataVencimentoAsc(
    	                    eventoId);

    	} else if (inicio != null && fim != null) {

    	    lista = repository
    	            .findByDataVencimentoBetweenOrderByDataVencimentoAsc(
    	                    inicio,
    	                    fim);

    	} else {

    	    lista = repository
    	            .findAllByOrderByDataVencimentoAsc();
    	}
        if (cartaoId != null) {
            lista = lista.stream()
                    .filter(l -> l.getCartao() != null
                            && cartaoId.equals(l.getCartao().getId()))
                    .toList();
        }
    
    	BigDecimal receitas = BigDecimal.ZERO;
    	BigDecimal despesas = BigDecimal.ZERO;

    	for (Lancamento l : lista) {

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

    	model.addAttribute("lista", lista);
    	model.addAttribute("totalReceitas", receitas);
    	model.addAttribute("totalDespesas", despesas);
    	model.addAttribute("saldoFiltro", saldo);

        model.addAttribute("eventoSelecionado", eventoId);
        model.addAttribute("cartaoSelecionado", cartaoId);
        model.addAttribute("inicioSelecionado", inicio);
        model.addAttribute("fimSelecionado", fim);

        carregarCombos(model);

        return "lancamentos";
    }

    @GetMapping("/lancamentos/novo")
    public String novo(Model model) {

        Lancamento lancamento = new Lancamento();

        lancamento.setCategoria(new Categoria());
        lancamento.setEvento(new EventoFinanceiro());

        model.addAttribute("lancamento", lancamento);
        carregarCombos(model);

        return "lancamento-form";
    }

    @PostMapping("/lancamentos/salvar")
    public String salvar(
            @Valid @ModelAttribute Lancamento lancamento,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            carregarCombos(model);
            return "lancamento-form";
        }

        if (lancamento.getEvento() != null
                && lancamento.getEvento().getId() != null) {

            lancamento.setEvento(
                    eventoRepository.findById(
                            lancamento.getEvento().getId())
                            .orElse(null));

        } else {
            lancamento.setEvento(null);
        }

        if (Boolean.TRUE.equals(lancamento.getRecorrente())) {
            lancamento.setEvento(null);
        }

        tratarRelacionamentos(lancamento);

        if (Boolean.TRUE.equals(lancamento.getParcelado())
                && lancamento.getTotalParcelas() != null
                && lancamento.getTotalParcelas() > 1) {

            if (lancamento.getEvento() == null
                    || lancamento.getEvento().getIndice() == null) {

                EventoFinanceiro eventoInicial =
                        eventoAutomaticoService.obterOuCriarEvento(
                                lancamento.getDataVencimento());

                lancamento.setEvento(eventoInicial);
            }

            BigDecimal valorParcela =
                    lancamento.getValor().divide(
                            BigDecimal.valueOf(lancamento.getTotalParcelas()),
                            2,
                            java.math.RoundingMode.HALF_UP);

            List<EventoFinanceiro> eventosParcelas =
                    eventoRepository.findByIndiceGreaterThanEqualOrderByIndiceAsc(
                            lancamento.getEvento().getIndice());

            for (int i = 1; i <= lancamento.getTotalParcelas(); i++) {

                Lancamento parcela = new Lancamento();

                parcela.setDescricao(
                        lancamento.getDescricao()
                                + " " + i + "/"
                                + lancamento.getTotalParcelas());

                parcela.setCategoria(lancamento.getCategoria());
                parcela.setCartao(lancamento.getCartao());
                parcela.setCompromisso(lancamento.getCompromisso());

                parcela.setTipo(lancamento.getTipo());
                parcela.setStatus(lancamento.getStatus());
                parcela.setValor(valorParcela);

                parcela.setParcelado(true);
                parcela.setRecorrente(false);
                parcela.setNumeroParcela(i);
                parcela.setTotalParcelas(lancamento.getTotalParcelas());
                parcela.setDiaRecorrencia(lancamento.getDiaRecorrencia());

                LocalDate dataParcela =
                        lancamento.getDataVencimento().plusMonths(i - 1);

                EventoFinanceiro eventoParcela =
                        eventoAutomaticoService.obterOuCriarEvento(dataParcela);

                parcela.setEvento(eventoParcela);
                parcela.setDataVencimento(dataParcela);

                repository.save(parcela);
            }

        } else {
            associarEventoAutomaticoReceita(lancamento);
            repository.save(lancamento);
            gerarReceitaRecorrenteEmEventosPagamento(lancamento);
        }

        return "redirect:/lancamentos";
    }

    @GetMapping("/lancamentos/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Lancamento lancamento =
                repository.findById(id).orElseThrow();

        System.out.println("DATA EDITAR = "
                + lancamento.getDataVencimento());
        
        System.err.println("############################");
        System.err.println("EDITANDO ID = " + id);
        System.err.println("DATA = " + lancamento.getDataVencimento());
        System.err.println("############################");

        if (lancamento.getCategoria() == null) {
            lancamento.setCategoria(new Categoria());
        }

        if (lancamento.getEvento() == null) {
            lancamento.setEvento(new EventoFinanceiro());
        }

        model.addAttribute("lancamento", lancamento);
        carregarCombos(model);

        return "lancamento-form";
    }

    @PostMapping("/lancamentos/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/lancamentos";
    }

    private void carregarCombos(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("eventos", eventoRepository.findAll());
        model.addAttribute("cartoes", cartaoRepository.findAll());
    }
    
    @GetMapping("/lancamentos/duplicar/{id}")
    public String duplicar(@PathVariable Long id) {

        Lancamento original =
                repository.findById(id).orElseThrow();

        Lancamento copia = new Lancamento();

        copia.setDescricao(original.getDescricao() + " - Cópia");
        copia.setValor(original.getValor());
        copia.setDataVencimento(original.getDataVencimento());
        copia.setDataPagamento(null);

        copia.setTipo(original.getTipo());
        copia.setStatus(original.getStatus());

        copia.setCategoria(original.getCategoria());
        copia.setEvento(original.getEvento());
        copia.setCartao(original.getCartao());
        copia.setCompromisso(original.getCompromisso());

        copia.setParcelado(false);
        copia.setRecorrente(false);
        copia.setTotalParcelas(null);
        copia.setNumeroParcela(null);
        copia.setDiaRecorrencia(original.getDiaRecorrencia());

        repository.save(copia);

        return "redirect:/lancamentos";
    }

    private void tratarRelacionamentos(Lancamento lancamento) {

        // Categoria
        if (lancamento.getCategoria() != null
                && lancamento.getCategoria().getId() != null) {

            Categoria categoria =
                    categoriaRepository.findById(
                            lancamento.getCategoria().getId())
                            .orElse(null);

            lancamento.setCategoria(categoria);

        } else {
            lancamento.setCategoria(null);
        }

        // Evento
        if (lancamento.getEvento() != null
                && lancamento.getEvento().getId() != null) {

            EventoFinanceiro evento =
                    eventoRepository.findById(
                            lancamento.getEvento().getId())
                            .orElse(null);

            lancamento.setEvento(evento);

        } else {
            lancamento.setEvento(null);
        }

        // Cartão
        if (lancamento.getCartao() != null
                && lancamento.getCartao().getId() != null) {

            lancamento.setCartao(
                    cartaoRepository.findById(
                            lancamento.getCartao().getId())
                            .orElse(null));

        } else {
            lancamento.setCartao(null);
        }

        // Compromisso
        if (lancamento.getCompromisso() != null
                && lancamento.getCompromisso().getId() == null) {

            lancamento.setCompromisso(null);
        }
    }

    private void gerarReceitaRecorrenteEmEventosPagamento(Lancamento receita) {

        if (receita.getTipo() != TipoLancamento.RECEITA
                || !Boolean.TRUE.equals(receita.getRecorrente())) {
            return;
        }

        List<EventoFinanceiro> eventosPagamento =
                eventoRepository.findByTipoOrderByDataEventoAsc(
                        TipoEventoFinanceiro.PAGAMENTO);

        for (EventoFinanceiro evento : eventosPagamento) {

            List<Lancamento> lancamentosEvento =
                    repository.findByEventoId(evento.getId());

            boolean jaExiste =
                    lancamentosEvento.stream()
                            .anyMatch(l ->
                                    l.getTipo() == TipoLancamento.RECEITA
                                    && l.getDescricao() != null
                                    && receita.getDescricao() != null
                                    && l.getDescricao().equals(receita.getDescricao()));

            if (jaExiste) {
                continue;
            }

            Lancamento novo = new Lancamento();

            novo.setDescricao(receita.getDescricao());
            novo.setValor(receita.getValor());
            novo.setTipo(receita.getTipo());
            novo.setStatus(receita.getStatus());
            novo.setCategoria(receita.getCategoria());
            novo.setCartao(receita.getCartao());
            novo.setCompromisso(receita.getCompromisso());

            novo.setEvento(evento);
            novo.setDataVencimento(evento.getDataEvento());

            novo.setRecorrente(false);
            novo.setParcelado(false);
            novo.setDiaRecorrencia(receita.getDiaRecorrencia());

            repository.save(novo);
        }
    }

    private void associarEventoAutomaticoReceita(Lancamento lancamento) {

        if (lancamento.getTipo() != TipoLancamento.RECEITA
                || Boolean.TRUE.equals(lancamento.getRecorrente())
                || lancamento.getDataVencimento() == null
                || lancamento.getEvento() != null) {
            return;
        }

        EventoFinanceiro evento =
                eventoAutomaticoService.obterOuCriarEvento(
                        lancamento.getDataVencimento());

        lancamento.setEvento(evento);
    }
    
   
}


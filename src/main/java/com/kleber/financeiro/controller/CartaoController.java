package com.kleber.financeiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kleber.financeiro.entity.Cartao;
import com.kleber.financeiro.repository.CartaoRepository;

@Controller
public class CartaoController {

    private final CartaoRepository repository;

    public CartaoController(CartaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cartoes")
    public String listar(Model model) {
        model.addAttribute("lista", repository.findAll());
        return "cartoes";
    }

    @GetMapping("/cartoes/novo")
    public String novo(Model model) {
        model.addAttribute("cartao", new Cartao());
        return "cartao-form";
    }

    @PostMapping("/cartoes/salvar")
    public String salvar(@ModelAttribute Cartao cartao) {
        repository.save(cartao);
        return "redirect:/cartoes";
    }

    @GetMapping("/cartoes/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cartao cartao = repository.findById(id).orElseThrow();
        model.addAttribute("cartao", cartao);
        return "cartao-form";
    }

    @GetMapping("/cartoes/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/cartoes";
    }
}
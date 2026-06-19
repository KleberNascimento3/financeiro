package com.kleber.financeiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kleber.financeiro.entity.Compromisso;
import com.kleber.financeiro.repository.CompromissoRepository;

@Controller
@RequestMapping("/compromissos")
public class CompromissoController {

    private final CompromissoRepository repository;

    public CompromissoController(
            CompromissoRepository repository) {

        this.repository = repository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "compromissos",
                repository.findAll());

        model.addAttribute(
                "compromisso",
                new Compromisso());

        return "compromissos";
    }

    @PostMapping
    public String salvar(
            @ModelAttribute Compromisso compromisso) {

        repository.save(compromisso);

        return "redirect:/compromissos";
    }
}
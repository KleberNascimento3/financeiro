package com.kleber.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kleber.financeiro.entity.Categoria;
import com.kleber.financeiro.repository.CategoriaRepository;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias",
                repository.findAll());

        model.addAttribute("categoria",
                new Categoria());

        return "categorias";
    }

    @PostMapping
    public String salvar(Categoria categoria) {

        repository.save(categoria);

        return "redirect:/categorias";
    }
}
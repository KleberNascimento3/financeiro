package com.kleber.financeiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kleber.financeiro.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/teste")
    public String teste() {
        return "eventos";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("receitas",
                dashboardService.totalReceitas());

        model.addAttribute("despesas",
                dashboardService.totalDespesas());

        model.addAttribute("saldo",
                dashboardService.saldo());

        model.addAttribute(
                "eventosResumo",
                dashboardService.resumoPorEvento());

        model.addAttribute(
                "fluxoMensal",
                dashboardService.fluxoMensal());

        return "dashboard";
    }
}
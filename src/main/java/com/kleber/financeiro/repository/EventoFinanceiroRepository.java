package com.kleber.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.EventoFinanceiro;

public interface EventoFinanceiroRepository
        extends JpaRepository<EventoFinanceiro, Long> {

}
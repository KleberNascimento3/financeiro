package com.kleber.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.EventoFinanceiro;

public interface EventoFinanceiroRepository
        extends JpaRepository<EventoFinanceiro, Long> {
	
	List<EventoFinanceiro> findByIndiceGreaterThanEqualOrderByIndiceAsc(
	        Integer indice);

}
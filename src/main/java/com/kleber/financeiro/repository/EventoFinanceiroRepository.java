package com.kleber.financeiro.repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.EventoFinanceiro;

public interface EventoFinanceiroRepository
        extends JpaRepository<EventoFinanceiro, Long> {
	
	List<EventoFinanceiro> findByIndiceGreaterThanEqualOrderByIndiceAsc(
	        Integer indice);
	
	Optional<EventoFinanceiro> findByDescricaoAndDataEvento(
            String descricao,
            LocalDate dataEvento);

    EventoFinanceiro findTopByOrderByIndiceDesc();

}
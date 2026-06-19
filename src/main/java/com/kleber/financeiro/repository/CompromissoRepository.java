package com.kleber.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.Compromisso;

public interface CompromissoRepository
        extends JpaRepository<Compromisso, Long> {

}
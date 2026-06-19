package com.kleber.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

}
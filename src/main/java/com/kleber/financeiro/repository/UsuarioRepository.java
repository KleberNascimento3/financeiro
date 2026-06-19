package com.kleber.financeiro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

}
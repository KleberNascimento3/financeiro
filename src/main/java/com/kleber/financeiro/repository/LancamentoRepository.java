package com.kleber.financeiro.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kleber.financeiro.entity.Lancamento;
import com.kleber.financeiro.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByEventoId(Long eventoId);

    List<Lancamento> findByRecorrenteTrue();

    List<Lancamento> findByRecorrenteTrueAndTipo(
            TipoLancamento tipo);

    List<Lancamento> findByRecorrenteTrueAndDiaRecorrencia(
            Integer diaRecorrencia);

    List<Lancamento> findAllByOrderByDataVencimentoAsc();

    List<Lancamento> findByDataVencimentoBetweenOrderByDataVencimentoAsc(
            LocalDate inicio,
            LocalDate fim);

    List<Lancamento> findByEventoIdOrderByDataVencimentoAsc(
            Long eventoId);

    List<Lancamento> findByEventoIdAndDataVencimentoBetweenOrderByDataVencimentoAsc(
            Long eventoId,
            LocalDate inicio,
            LocalDate fim);
}

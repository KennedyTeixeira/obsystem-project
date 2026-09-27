package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Classificacao;
import com.obsystem.entities.enums.ClassificacaoStatus;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Integer> {

    List<Classificacao> findByStatus(ClassificacaoStatus status);

    List<Classificacao> findAllByOrderByDescricaoAsc();
}

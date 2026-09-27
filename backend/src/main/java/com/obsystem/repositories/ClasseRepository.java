package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Classe;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Integer> {
    List<Classe> findByStatusOrderByDescricaoAsc(ProdutoStatus status);
    List<Classe> findAllByOrderByDescricaoAsc();
}

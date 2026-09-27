package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Modelo;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
    List<Modelo> findByStatusOrderByDescricaoAsc(ProdutoStatus status);
    List<Modelo> findAllByOrderByDescricaoAsc();
}

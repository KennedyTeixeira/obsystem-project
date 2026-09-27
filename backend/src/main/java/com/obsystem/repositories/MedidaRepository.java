package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Medida;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface MedidaRepository extends JpaRepository<Medida, Integer> {
    List<Medida> findByStatusOrderByDescricaoAsc(ProdutoStatus status);
    List<Medida> findAllByOrderByDescricaoAsc();
}

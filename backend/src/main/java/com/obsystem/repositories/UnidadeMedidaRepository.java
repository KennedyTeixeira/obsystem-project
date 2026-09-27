package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.UnidadeMedida;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {
    List<UnidadeMedida> findByStatusOrderBySiglaAsc(ProdutoStatus status);
    List<UnidadeMedida> findAllByOrderBySiglaAsc();
}

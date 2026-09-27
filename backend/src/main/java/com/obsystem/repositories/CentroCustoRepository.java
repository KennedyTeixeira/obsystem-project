package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.CentroCusto;
import com.obsystem.entities.enums.CentroCustoStatus;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Integer> {

    List<CentroCusto> findByStatus(CentroCustoStatus status);

    List<CentroCusto> findAllByOrderByDescricaoAsc();
}

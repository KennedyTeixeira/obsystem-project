package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.PlanoConta;
import com.obsystem.entities.enums.PlanoContaStatus;

@Repository
public interface PlanoContaRepository extends JpaRepository<PlanoConta, Integer> {

    List<PlanoConta> findByStatus(PlanoContaStatus status);

    List<PlanoConta> findAllByOrderByDescricaoAsc();
}

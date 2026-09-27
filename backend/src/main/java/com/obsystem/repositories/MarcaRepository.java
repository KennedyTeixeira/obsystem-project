package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Marca;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    List<Marca> findByStatusOrderByDescricaoAsc(ProdutoStatus status);
    List<Marca> findAllByOrderByDescricaoAsc();
}

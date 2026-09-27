package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.SubCategoria;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {
    List<SubCategoria> findByStatusOrderByDescricaoAsc(ProdutoStatus status);
    List<SubCategoria> findAllByOrderByDescricaoAsc();
    List<SubCategoria> findByCategoriaIdOrderByDescricaoAsc(Integer categoriaId);
}

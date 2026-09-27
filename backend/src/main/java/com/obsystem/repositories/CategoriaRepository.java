package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Categoria;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByStatusOrderByDescricaoAsc(ProdutoStatus status);
    List<Categoria> findAllByOrderByDescricaoAsc();
}

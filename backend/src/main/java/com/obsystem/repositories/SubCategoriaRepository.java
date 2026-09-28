package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.SubCategoria;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {
    List<SubCategoria> findByStatusOrderByDescricaoAsc(ProdutoStatus status);

    @Query("SELECT DISTINCT s FROM SubCategoria s LEFT JOIN FETCH s.categorias ORDER BY s.descricao ASC")
    List<SubCategoria> findAllWithCategorias();

    @Query("SELECT DISTINCT s FROM SubCategoria s JOIN FETCH s.categorias c WHERE c.id = :categoriaId ORDER BY s.descricao ASC")
    List<SubCategoria> findByCategoriaId(@Param("categoriaId") Integer categoriaId);
}

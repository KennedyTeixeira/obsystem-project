package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.TipoProduto;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Integer> {
    List<TipoProduto> findByStatusOrderByNomeAsc(ProdutoStatus status);
    List<TipoProduto> findAllByOrderByNomeAsc();
}

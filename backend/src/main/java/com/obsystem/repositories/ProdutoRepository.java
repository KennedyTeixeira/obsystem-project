package com.obsystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Produto;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
    Optional<Produto> findByCodigoProduto(String codigoProduto);
    
    boolean existsByCodigoProduto(String codigoProduto);
    
    boolean existsByCodigoProdutoAndIdNot(String codigoProduto, Integer id);
    
    List<Produto> findByNomeProdutoContainingIgnoreCase(String nome);

    List<Produto> findByNomeProdutoContainingIgnoreCaseOrCodigoProdutoContainingIgnoreCase(String nome, String codigo);
    
    List<Produto> findByStatusProduto(ProdutoStatus status);
}

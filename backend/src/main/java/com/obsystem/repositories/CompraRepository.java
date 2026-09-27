package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Compra;
import com.obsystem.entities.enums.CompraStatus;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @Query("SELECT c FROM Compra c ORDER BY c.emissao DESC, c.id DESC")
    List<Compra> findAllOrderByEmissaoDesc();

    List<Compra> findByStatusCompra(CompraStatus status);

    @Query("SELECT c FROM Compra c WHERE (:search IS NULL OR LOWER(c.fornecedor.pessoa.nome) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:status IS NULL OR c.statusCompra = :status) ORDER BY c.emissao DESC, c.id DESC")
    List<Compra> searchCompras(@Param("search") String search, @Param("status") CompraStatus status);
}

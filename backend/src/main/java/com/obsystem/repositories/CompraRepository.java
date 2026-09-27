package com.obsystem.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Query("SELECT c FROM Compra c WHERE LOWER(c.fornecedor.pessoa.nome) LIKE :search AND (:status IS NULL OR c.statusCompra = :status) ORDER BY c.emissao DESC, c.id DESC")
    List<Compra> searchComprasWithText(@Param("search") String search, @Param("status") CompraStatus status);

    @Query("SELECT c FROM Compra c WHERE c.statusCompra = :status ORDER BY c.emissao DESC, c.id DESC")
    List<Compra> findByStatusCompraOrderByEmissaoDesc(@Param("status") CompraStatus status);

    List<Compra> findTop5ByOrderByEmissaoDescIdDesc();

    @Query("SELECT COALESCE(SUM(c.totalCompra), 0) FROM Compra c WHERE c.emissao BETWEEN :inicio AND :fim")
    BigDecimal sumTotalByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT COUNT(c) FROM Compra c WHERE c.emissao BETWEEN :inicio AND :fim")
    Long countByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}

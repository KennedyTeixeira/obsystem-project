package com.obsystem.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Cliente;
import com.obsystem.entities.Venda;
import com.obsystem.entities.enums.VendaStatus;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {

    List<Venda> findByCliente(Cliente cliente);

    List<Venda> findByStatusVenda(VendaStatus statusVenda);

    @Query("SELECT v FROM Venda v JOIN v.cliente c JOIN c.pessoa p " +
           "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "ORDER BY v.emissao DESC, v.id DESC")
    List<Venda> searchByClienteNome(@Param("search") String search);

    @Query("SELECT v FROM Venda v ORDER BY v.emissao DESC, v.id DESC")
    List<Venda> findAllOrderByEmissaoDesc();

    List<Venda> findTop5ByOrderByEmissaoDescIdDesc();

    @Query("SELECT COALESCE(SUM(v.totalVenda), 0) FROM Venda v WHERE v.emissao BETWEEN :inicio AND :fim")
    BigDecimal sumTotalByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.emissao BETWEEN :inicio AND :fim")
    Long countByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}

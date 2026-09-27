package com.obsystem.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Compra;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.Titulo;
import com.obsystem.entities.Venda;
import com.obsystem.entities.enums.TipoTitulo;
import com.obsystem.entities.enums.TituloStatus;

@Repository
public interface TituloRepository extends JpaRepository<Titulo, Integer> {

    List<Titulo> findByVenda(Venda venda);

    List<Titulo> findByCompra(Compra compra);

    List<Titulo> findByPessoa(Pessoa pessoa);

    List<Titulo> findByStatus(TituloStatus status);

    @Query("SELECT t FROM Titulo t WHERE " +
           "(LOWER(t.descricao) LIKE :search OR LOWER(t.pessoa.nome) LIKE :search) AND " +
           "(:tipo IS NULL OR t.tipo = :tipo) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:dataInicio IS NULL OR t.vencimento >= :dataInicio) AND " +
           "(:dataFim IS NULL OR t.vencimento <= :dataFim) " +
           "ORDER BY t.vencimento ASC, t.id DESC")
    List<Titulo> searchTitulosWithText(@Param("search") String search,
                                       @Param("tipo") TipoTitulo tipo,
                                       @Param("status") TituloStatus status,
                                       @Param("dataInicio") LocalDate dataInicio,
                                       @Param("dataFim") LocalDate dataFim);

    @Query("SELECT t FROM Titulo t WHERE " +
           "(:tipo IS NULL OR t.tipo = :tipo) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:dataInicio IS NULL OR t.vencimento >= :dataInicio) AND " +
           "(:dataFim IS NULL OR t.vencimento <= :dataFim) " +
           "ORDER BY t.vencimento ASC, t.id DESC")
    List<Titulo> searchTitulosWithoutText(@Param("tipo") TipoTitulo tipo,
                                          @Param("status") TituloStatus status,
                                          @Param("dataInicio") LocalDate dataInicio,
                                          @Param("dataFim") LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(t.valorTotal), 0) FROM Titulo t WHERE t.tipo = :tipo AND t.status = :status")
    BigDecimal sumByTipoAndStatus(@Param("tipo") TipoTitulo tipo, @Param("status") TituloStatus status);

    @Query("SELECT COALESCE(SUM(t.valorTotal), 0) FROM Titulo t WHERE t.tipo = :tipo AND t.status = :status AND t.pagamento BETWEEN :inicio AND :fim")
    BigDecimal sumPagoByTipoAndPeriodo(@Param("tipo") TipoTitulo tipo,
                                       @Param("status") TituloStatus status,
                                       @Param("inicio") LocalDate inicio,
                                       @Param("fim") LocalDate fim);

    @Query("SELECT COALESCE(SUM(t.valorTotal), 0) FROM Titulo t WHERE t.status = 'PENDENTE' AND t.vencimento < :hoje")
    BigDecimal sumVencidos(@Param("hoje") LocalDate hoje);

    @Query("SELECT COUNT(t) FROM Titulo t WHERE t.status = 'PENDENTE' AND t.vencimento < :hoje")
    Long countVencidos(@Param("hoje") LocalDate hoje);

    List<Titulo> findTop5ByStatusOrderByVencimentoAscIdDesc(TituloStatus status);
}

package com.obsystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Fornecedor;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.enums.FornecedorStatus;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

    Optional<Fornecedor> findByPessoa(Pessoa pessoa);

    boolean existsByPessoa(Pessoa pessoa);

    List<Fornecedor> findByStatus(FornecedorStatus status);

    @Query("SELECT f FROM Fornecedor f JOIN f.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Fornecedor> searchByNome(@Param("search") String search);
}

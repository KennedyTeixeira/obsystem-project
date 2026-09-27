package com.obsystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Cliente;
import com.obsystem.entities.Pessoa;
import com.obsystem.entities.enums.ClienteStatus;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByPessoa(Pessoa pessoa);

    boolean existsByPessoa(Pessoa pessoa);

    List<Cliente> findByStatus(ClienteStatus status);

    @Query("SELECT c FROM Cliente c JOIN c.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Cliente> searchByNome(@Param("search") String search);
}

package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Pessoa;
import com.obsystem.entities.Titulo;
import com.obsystem.entities.Venda;
import com.obsystem.entities.enums.TituloStatus;

@Repository
public interface TituloRepository extends JpaRepository<Titulo, Integer> {

    List<Titulo> findByVenda(Venda venda);

    List<Titulo> findByPessoa(Pessoa pessoa);

    List<Titulo> findByStatus(TituloStatus status);
}

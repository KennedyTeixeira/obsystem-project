package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.Milimetro;
import com.obsystem.entities.enums.ProdutoStatus;

@Repository
public interface MilimetroRepository extends JpaRepository<Milimetro, Integer> {
    List<Milimetro> findByStatusOrderByEspessuraAsc(ProdutoStatus status);
    List<Milimetro> findAllByOrderByEspessuraAsc();
}

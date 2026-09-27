package com.obsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.ItemVenda;
import com.obsystem.entities.Venda;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Integer> {

    List<ItemVenda> findByVenda(Venda venda);
}

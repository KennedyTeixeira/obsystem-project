package com.obsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obsystem.entities.ItemCompra;

@Repository
public interface ItemCompraRepository extends JpaRepository<ItemCompra, Integer> {
}

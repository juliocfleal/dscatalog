package com.julioleal.Ds.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julioleal.Ds.catalog.entities.Product;

@Repository
public interface ProductRepository extends  JpaRepository<Product, Long>{

}

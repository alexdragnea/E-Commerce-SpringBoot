package net.dg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.dg.model.Product;

public interface ProductRepository extends 
		JpaRepository<Product, Long> {

}

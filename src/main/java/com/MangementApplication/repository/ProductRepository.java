package com.MangementApplication.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.MangementApplication.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

	
    // Add custom query methods if needed
    boolean existsByName(String name);
    Product findByNameAndIdNot(String name, Long id);


	
}



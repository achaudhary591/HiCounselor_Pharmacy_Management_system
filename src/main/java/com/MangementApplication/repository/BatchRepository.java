package com.MangementApplication.repository;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.MangementApplication.entity.Batch;
import com.MangementApplication.entity.Product;


public interface BatchRepository extends JpaRepository <Batch, Long> {

	
    // Add custom query methods if needed
    boolean existsByProductAndExpiryDate(Product product, LocalDate expiryDate);
    List<Batch> findByProduct(Product product);
    

	
}


package com.MangementApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MangementApplication.entity.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    // Add any additional query methods if needed
}


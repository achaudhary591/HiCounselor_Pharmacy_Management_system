package com.MangementApplication.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "batch")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ToString.Exclude
    private Product product;
    private int qty;
    private LocalDate expiryDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Batch(Product product, int qty, LocalDate expiryDate, LocalDate createdAt, LocalDate updatedAt) {
        this.product = product;
        this.qty = qty;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Batch() {
        // Default constructor
    }


    //Getter , Setter and Constructor

}

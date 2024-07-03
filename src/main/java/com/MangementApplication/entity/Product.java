package com.MangementApplication.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int qty;
    private double price;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Batch> batches = new ArrayList<>();


    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.qty = 0;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    //Getter , Setter and Constructor
}

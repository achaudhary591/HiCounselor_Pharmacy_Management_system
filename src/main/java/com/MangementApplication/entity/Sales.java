package com.MangementApplication.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="sales")
@Data
@NoArgsConstructor
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleItems> saleItems = new ArrayList<>();  // Initialize the list

    @Column(name = "sale_date")
    private LocalDate saleDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Override
    public String toString() {
        return "Sales{" +
                "saleId=" + saleId +
                ", saleDate=" + saleDate +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                '}';
    }

    // Getter, Setter, and Constructor
}



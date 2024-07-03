package com.MangementApplication.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="salesItems")
@Data
@NoArgsConstructor
public class SaleItems {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_item_id")
    private Long saleItemId;

    @ManyToOne
    @JoinColumn(name = "sale_id", referencedColumnName = "sale_id")
    @JsonBackReference
    private Sales sale;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "unit_price")
  private BigDecimal unitPrice;

  @Column(name = "subtotal")
  private BigDecimal subtotal;

  @Column(name = "created_at")
  private LocalDate createdAt;

  @Override
    public String toString() {
        return "SaleItems{" +
                "saleItemId=" + saleItemId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", createdAt=" + createdAt +
                '}';
    }

  //Getter , Setter and Constructor
}

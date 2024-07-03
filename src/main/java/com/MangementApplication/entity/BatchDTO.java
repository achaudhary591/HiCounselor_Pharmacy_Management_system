package com.MangementApplication.entity;

import java.time.LocalDate;

public class BatchDTO {
    private Long batchId;
    private Long productId;
    private int qty;
    private LocalDate expiryDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Constructors, getters, and setters...

    public BatchDTO() {
        // Default constructor
    }

    public BatchDTO(Long batchId, Long productId, int qty, LocalDate expiryDate, LocalDate createdAt, LocalDate updatedAt) {
        this.batchId = batchId;
        this.productId = productId;
        this.qty = qty;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}


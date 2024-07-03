package com.MangementApplication.entity;



import java.time.LocalDate;

public class RequestDTO {
    
    private int qty;
    private LocalDate expiryDate;

    public RequestDTO() {
    }

    public RequestDTO( int qty, LocalDate expiryDate) {
       
        this.qty = qty;
        this.expiryDate = expiryDate;
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
}


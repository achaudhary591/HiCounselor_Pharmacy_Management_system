package com.MangementApplication.entity;

public class ProductRequestDTO {
    private String name;
    private double price;

    // Constructor, getters, and setters

    public ProductRequestDTO(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


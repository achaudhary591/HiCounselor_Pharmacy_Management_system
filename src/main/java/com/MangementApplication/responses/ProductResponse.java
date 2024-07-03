package com.MangementApplication.responses;

import lombok.Data;

import java.time.LocalDate;

/*///Response
{
        "id": 1,
        "name": "Product 1",
        "qty": 0,
        "price": 14.99,
        "createdAt": "2023-08-14",
        "updatedAt": "2023-08-14"
        }*/
@Data
public class ProductResponse {

    private Long id;
    private String name;
    private int qty;
    private double price;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

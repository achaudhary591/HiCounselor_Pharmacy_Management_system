package com.MangementApplication.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.MangementApplication.entity.SaleItems;

import lombok.Data;

@Data
public class OrderResponse {

    private Long saleId;

    private List<SaleItems> saleItems = new ArrayList<>(); 
    private LocalDate saleDate;

    private BigDecimal totalAmount;

    private LocalDate createdAt;


}

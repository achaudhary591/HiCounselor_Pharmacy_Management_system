package com.MangementApplication.entity;

import java.util.List;

public class OrderDto {
    private List<SaleItemDto> saleItems;

    public OrderDto() {
    }

    public OrderDto(List<SaleItemDto> saleItems) {
        this.saleItems = saleItems;
    }

    public List<SaleItemDto> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItemDto> saleItems) {
        this.saleItems = saleItems;
    }
}


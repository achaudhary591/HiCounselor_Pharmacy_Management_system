package com.MangementApplication.requests;

import java.util.List;

import com.MangementApplication.entity.SaleItemDto;

public class SaleItemsRequest {
    private List<SaleItemDto> saleItems;

    public List<SaleItemDto> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItemDto> saleItems) {
        this.saleItems = saleItems;
    }
}

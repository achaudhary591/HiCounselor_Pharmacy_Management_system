package com.MangementApplication.responses;

import lombok.Data;

import java.time.LocalDate;

/*{
    "batchId": 14,
    "qty": 100,
    "expiryDate": "2025-09-10",
    "createdAt": "2024-07-03",
    "updatedAt": "2024-07-03"
}*/
@Data
public class BatchResponse {
    private long batchId;
    private long productId;
    private int qty;
    private LocalDate expiryDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

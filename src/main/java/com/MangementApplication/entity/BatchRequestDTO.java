package com.MangementApplication.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BatchRequestDTO {

    @NonNull
    private int qty;
    @NonNull
    private LocalDate expiryDate;


    public BatchRequestDTO() {
    }

    public BatchRequestDTO(int qty, LocalDate expiryDate) {

        this.qty = qty;
        this.expiryDate = expiryDate;

    }


}

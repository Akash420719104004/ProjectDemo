package com.company.mongoDb.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SaleDto {
    private String id;
    private String productId;
    private String branchId;
    private int quantity;
    private Double totalAmount;
    private LocalDate saleDate;
}

package com.company.mongoDb.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaleAnalyticsDto {
    private int count;
    private double totalRevenue;
    private List<SaleDto> sales;
}

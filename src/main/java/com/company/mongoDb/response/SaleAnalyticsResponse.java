package com.company.mongoDb.response;

import com.company.mongoDb.dto.SaleDto;
import lombok.Data;

import java.util.List;
@Data
public class SaleAnalyticsResponse {
    private long totalCount;
    private double totalRevenue;
    private List<SaleDto> sales;
}

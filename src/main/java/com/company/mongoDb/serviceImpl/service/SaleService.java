package com.company.mongoDb.serviceImpl.service;

import com.company.mongoDb.dto.SaleAnalyticsDto;
import com.company.mongoDb.dto.SaleDto;
import com.company.mongoDb.dto.SaleEmailDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.response.SaleAnalyticsResponse;

import java.time.LocalDate;
import java.util.List;

public interface SaleService {
    SaleEmailDto createSale(SaleEmailDto saleDto) throws CustomValidationException;

    List<SaleDto> getAllSales()throws CustomValidationException;

    SaleAnalyticsDto getSalesAnalytics(LocalDate startDate, LocalDate endDate)throws CustomValidationException;

    SaleAnalyticsResponse getBranchSalesAnalytics(String branchId, LocalDate startDate, LocalDate endDate)throws CustomValidationException;

    SaleAnalyticsResponse getProductSalesAnalytics(String productId, LocalDate startDate, LocalDate endDate)throws CustomValidationException;

    List<SaleAnalyticsResponse> getDailySalesTrend(LocalDate startDate, LocalDate endDate)throws CustomValidationException;
    public boolean verifySaleOtp(String saleId, String enteredOtp) throws CustomValidationException;
}



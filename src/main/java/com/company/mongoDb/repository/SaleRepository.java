package com.company.mongoDb.repository;

import com.company.mongoDb.model.Sale;
import com.company.mongoDb.response.SaleAnalyticsResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends MongoRepository<Sale, String> {
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Sale> findByBranchIdAndSaleDateBetween(String branchId, LocalDateTime start, LocalDateTime end);
    List<Sale> findByProductIdAndSaleDateBetween(String productId, LocalDateTime start, LocalDateTime end);

  //  List<SaleAnalyticsResponse> getDailySalesTrend(LocalDate startDate, LocalDate endDate);
}


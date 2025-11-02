package com.company.mongoDb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "sale")
public class Sale {
    @Id
    private String id;
    private String branchId;
    private String productId;
    private int quantity;
    private double amount;
    private LocalDateTime saleDate;
    private String otpCode;
}

package com.company.mongoDb.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String sku;
    private String category;
    private String imageUrl;
    private String price;
    private List<String> branchId;
}

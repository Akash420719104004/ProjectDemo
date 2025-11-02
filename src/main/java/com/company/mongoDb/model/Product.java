package com.company.mongoDb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    private String name;
    private String sku;
    private String category;
    private String imageUrl;
    private String price;
    @DocumentReference
    private List<Branch > branchId;
}

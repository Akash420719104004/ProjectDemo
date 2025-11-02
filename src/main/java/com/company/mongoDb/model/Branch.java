package com.company.mongoDb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document(collection = "branch")
public class Branch {
    @Id
    private String id;
    private String name;
    private String address;
    @DocumentReference
    private List< Product> productId;
}

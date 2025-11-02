package com.company.mongoDb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "employee")
public class Employee {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
}

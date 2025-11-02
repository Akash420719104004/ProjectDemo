package com.company.mongoDb.dto;

import lombok.Data;

import java.util.List;

@Data
public class BranchDto {
    private String id;
    private String name;
    private String address;
    private List<String> productId;
}

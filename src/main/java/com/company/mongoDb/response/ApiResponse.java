package com.company.mongoDb.response;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private long count;
    private List<T> data;
}

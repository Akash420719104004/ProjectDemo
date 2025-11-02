package com.company.mongoDb.serviceImpl.service;

import com.company.mongoDb.dto.EmployeeDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.request.LoginRequest;

import java.util.Optional;

public interface EmployeeService {
    EmployeeDto register(EmployeeDto userDto) throws CustomValidationException;
   Optional< String> login(LoginRequest request) throws CustomValidationException;


//Object loadUserByUsername(String username);
}

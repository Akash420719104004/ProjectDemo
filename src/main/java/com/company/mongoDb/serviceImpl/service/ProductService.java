package com.company.mongoDb.serviceImpl.service;

import com.company.mongoDb.dto.ProductDto;
import com.company.mongoDb.exception.CustomValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto) throws CustomValidationException;
    ProductDto getProductById(String id) throws CustomValidationException;
    List<ProductDto> getAllProducts() throws CustomValidationException;
    ProductDto updateProduct(String id, ProductDto productDto) throws CustomValidationException;
    void deleteProduct(String id) throws CustomValidationException;
}

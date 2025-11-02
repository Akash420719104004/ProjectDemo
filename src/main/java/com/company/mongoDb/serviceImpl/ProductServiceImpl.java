package com.company.mongoDb.serviceImpl;

import com.company.mongoDb.controller.AuthController;
import com.company.mongoDb.dto.ProductDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.exception.ErrorCode;
import com.company.mongoDb.model.Branch;
import com.company.mongoDb.model.Product;
import com.company.mongoDb.repository.BranchRepository;
import com.company.mongoDb.repository.ProductRepository;
import com.company.mongoDb.serviceImpl.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    AuthController authController;

    @Override
    public ProductDto createProduct(ProductDto productDto)throws CustomValidationException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setSku(productDto.getSku());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setImageUrl(productDto.getImageUrl());
        List<Branch> branches = branchRepository.findAllById(productDto.getBranchId());
        if (branches.isEmpty()) {
            throw new RuntimeException("No valid branches found for IDs: " + productDto.getBranchId());
        }
        product.setBranchId(branches);

        productRepository.save(product);
        productDto.setId(product.getId());
        return productDto;
    }

    private String saveImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String uploadDir = "uploads/";
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            Path filePath = Paths.get(uploadDir, uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }


    @Override
    public ProductDto getProductById(String id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return null;
        }

        Product product = productOpt.get();
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        if (product.getBranchId() != null) {
            dto.setBranchId(product.getBranchId()
                    .stream()
                    .map(Branch::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setSku(product.getSku());
                    dto.setCategory(product.getCategory());
                    dto.setPrice(product.getPrice());
                    dto.setImageUrl(product.getImageUrl());
                    if (product.getBranchId() != null) {
                        dto.setBranchId(product.getBranchId()
                                .stream()
                                .map(Branch::getId)
                                .collect(Collectors.toList()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(String id, ProductDto productDto) {
        Optional<Product> existingOpt = productRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }

        Product product = existingOpt.get();
        product.setName(productDto.getName());
        product.setSku(productDto.getSku());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());

        if (productDto.getBranchId() != null && !productDto.getBranchId().isEmpty()) {
            List<Branch> branches = branchRepository.findAllById(productDto.getBranchId());
            product.setBranchId(branches);
        }

        productRepository.save(product);
        productDto.setId(product.getId());
        return productDto;
    }

    @Override
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}

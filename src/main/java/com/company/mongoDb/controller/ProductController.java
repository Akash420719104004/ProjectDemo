package com.company.mongoDb.controller;

import com.company.mongoDb.dto.ProductDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.model.Product;
import com.company.mongoDb.repository.ProductRepository;
import com.company.mongoDb.serviceImpl.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping(value = "/add-product")
    public ResponseEntity<ProductDto> createProduct (
            @RequestBody ProductDto productDto
    ) throws CustomValidationException {

        ProductDto savedProduct = productService.createProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }


    @GetMapping("/products")
    public List<ProductDto> getAllProducts()throws CustomValidationException {
        return productService.getAllProducts();
    }

    @GetMapping("/fetch-by-id")
    public ProductDto getProductById(@RequestParam String id) throws CustomValidationException {
        return productService.getProductById(id);
    }

    @PutMapping("/update-by-productid")
    public ProductDto updateProduct(@RequestParam String id, @RequestBody ProductDto productDto)
    throws  CustomValidationException{
        return productService.updateProduct(id, productDto);
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam("productId") String productId
    ) throws CustomValidationException {

        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("error", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

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

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setImageUrl(filePath.toString());
            productRepository.save(product);

            response.put("message", "File uploaded and linked to product successfully");
            response.put("productId", productId);
            response.put("originalFileName", originalFilename);
            response.put("savedFileName", uniqueFileName);
            response.put("filePath", filePath.toAbsolutePath().toString());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Could not upload file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
//    @GetMapping("/images/{fileName}")
//    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
//            UrlResource resource = new UrlResource(filePath.toUri());
//
//            if (!resource.exists()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) contentType = "application/octet-stream";
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .body((Resource) resource);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @DeleteMapping("/delete-productid")
    public String deleteProduct(@RequestParam String id) throws CustomValidationException{
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}

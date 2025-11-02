package com.company.mongoDb.controller;

import com.company.mongoDb.dto.EmployeeDto;

import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.request.LoginRequest;
import com.company.mongoDb.serviceImpl.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AuthController {

    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Optional<String> login(@RequestBody LoginRequest request) throws CustomValidationException {
       return employeeService.login(request);
    }
    @PostMapping("/add-employee")
    public EmployeeDto register(@RequestBody EmployeeDto employeeDto) throws CustomValidationException {
    return employeeService.register(employeeDto);
}

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestPart("file") MultipartFile file)throws CustomValidationException {
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

            response.put("message", "File uploaded successfully");
            response.put("originalFileName", originalFilename);
            response.put("savedFileName", uniqueFileName);
            response.put("filePath", filePath.toAbsolutePath().toString());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Could not upload file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

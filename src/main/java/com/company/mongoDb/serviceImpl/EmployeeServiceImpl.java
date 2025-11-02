package com.company.mongoDb.serviceImpl;

import com.company.mongoDb.dto.EmployeeDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.exception.ErrorCode;
import com.company.mongoDb.model.Employee;
import com.company.mongoDb.repository.EmployeeRepository;
import com.company.mongoDb.request.LoginRequest;
import com.company.mongoDb.serviceImpl.service.EmployeeService;
import com.company.mongoDb.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public EmployeeDto register(EmployeeDto employeeDto) throws CustomValidationException {
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new CustomValidationException(ErrorCode.CAP_1005);
            
        }

        Employee employee = new Employee();
        employee.setEmail(employeeDto.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        employee.setUsername(employeeDto.getUsername());
        employeeRepository.save(employee);

        return employeeDto;
    }


   @Override
    public Optional<String> login(LoginRequest request) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(request.getEmail());

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
                String token = jwtUtil.generateToken(employee.getEmail());
                return Optional.of(token);
            } else {
                return Optional.of("Invalid credentials");
            }
        }
        return Optional.of("User not found");
    }




}

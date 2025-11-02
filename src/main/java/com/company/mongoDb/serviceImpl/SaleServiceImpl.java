package com.company.mongoDb.serviceImpl;

import com.company.mongoDb.dto.SaleDto;
import com.company.mongoDb.dto.SaleAnalyticsDto;
import com.company.mongoDb.dto.SaleEmailDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.model.Sale;
import com.company.mongoDb.repository.SaleRepository;
import com.company.mongoDb.response.SaleAnalyticsResponse;
import com.company.mongoDb.serviceImpl.service.SaleService;
import com.company.mongoDb.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpUtils otpUtils;


    @Override
    public SaleEmailDto createSale(SaleEmailDto saleDto) throws CustomValidationException {
        Sale sale = new Sale();
        sale.setProductId(saleDto.getProductId());
        sale.setBranchId(saleDto.getBranchId());
        sale.setQuantity(saleDto.getQuantity());
        sale.setAmount(saleDto.getTotalAmount());
        sale.setSaleDate(LocalDateTime.now());
        String otp = otpUtils.generateOtp(6);
        sale.setOtpCode(otp);
        saleRepository.save(sale);
        if (saleDto.getCustomerEmail() != null && !saleDto.getCustomerEmail().isBlank()) {
            emailService.sendEmail(
                    saleDto.getCustomerEmail(),
                    "Your Sale OTP",
                    "Your OTP for sale is: " + otp
            );
        }
        saleDto.setId(sale.getId());
        saleDto.setSaleDate(LocalDate.from(sale.getSaleDate()));
        return saleDto;
    }


    @Override
    public List<SaleDto> getAllSales()throws CustomValidationException {
        return saleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SaleAnalyticsDto getSalesAnalytics(LocalDate startDate, LocalDate endDate) throws CustomValidationException{
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);
        List<SaleDto> saleDtos = sales.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getAmount)
                .sum();
        SaleAnalyticsDto analytics = new SaleAnalyticsDto();
        analytics.setCount(sales.size());
        analytics.setTotalRevenue(totalRevenue);
        analytics.setSales(saleDtos);
        return analytics;
    }

    private SaleDto mapToDto(Sale sale) {
        SaleDto dto = new SaleDto();
        dto.setId(sale.getId());
        dto.setProductId(sale.getProductId());
        dto.setBranchId(sale.getBranchId());
        dto.setQuantity(sale.getQuantity());
        dto.setTotalAmount(sale.getAmount());
        dto.setSaleDate(sale.getSaleDate().toLocalDate());
        return dto;
    }
    @Override
    public SaleAnalyticsResponse getBranchSalesAnalytics(String branchId, LocalDate startDate, LocalDate endDate) throws CustomValidationException {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<Sale> sales = saleRepository.findByBranchIdAndSaleDateBetween(branchId, start, end);

        List<SaleDto> saleDtos = sales.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getAmount)
                .sum();

        SaleAnalyticsResponse response = new SaleAnalyticsResponse();
        response.setTotalCount(sales.size());
        response.setTotalRevenue(totalRevenue);
        response.setSales(saleDtos);

        return response;
    }
    @Override
    public SaleAnalyticsResponse getProductSalesAnalytics(String productId, LocalDate startDate, LocalDate endDate) throws CustomValidationException {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<Sale> sales = saleRepository.findByProductIdAndSaleDateBetween(productId, start, end);

        List<SaleDto> saleDtos = sales.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getAmount)
                .sum();

        SaleAnalyticsResponse response = new SaleAnalyticsResponse();
        response.setTotalCount(sales.size());
        response.setTotalRevenue(totalRevenue);
        response.setSales(saleDtos);

        return response;
    }
    @Override
    public List<SaleAnalyticsResponse> getDailySalesTrend(LocalDate startDate, LocalDate endDate) throws CustomValidationException{
        return startDate.datesUntil(endDate.plusDays(1))
                .map(date -> {
                    List<Sale> dailySales = saleRepository.findBySaleDateBetween(
                            date.atStartOfDay(), date.atTime(23, 59, 59));
                    double totalRevenue = dailySales.stream()
                            .mapToDouble(Sale::getAmount)
                            .sum();

                    SaleAnalyticsResponse response = new SaleAnalyticsResponse();
                    response.setTotalCount(dailySales.size());
                    response.setTotalRevenue(totalRevenue);
                    response.setSales(
                            dailySales.stream().map(this::mapToDto).collect(Collectors.toList())
                    );

                    return response;
                })
                .collect(Collectors.toList());
    }
    @Override
    public boolean verifySaleOtp(String saleId, String enteredOtp) throws CustomValidationException {
        Optional<Sale> saleOpt = saleRepository.findById(saleId);

        if (saleOpt.isEmpty()) {
            throw new RuntimeException("Sale not found");
        }

        Sale sale = saleOpt.get();
        if (sale.getOtpCode() != null && sale.getOtpCode().equals(enteredOtp)) {
            sale.setOtpCode(null);
            saleRepository.save(sale);
            return true;
        }

        return false;
    }



}

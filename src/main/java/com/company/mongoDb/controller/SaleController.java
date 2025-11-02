package com.company.mongoDb.controller;

import com.company.mongoDb.dto.SaleAnalyticsDto;
import com.company.mongoDb.dto.SaleDto;
import com.company.mongoDb.dto.SaleEmailDto;
import com.company.mongoDb.exception.CustomValidationException;
import com.company.mongoDb.response.SaleAnalyticsResponse;
import com.company.mongoDb.serviceImpl.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public SaleEmailDto createSale(@RequestBody SaleEmailDto saleDto)throws CustomValidationException {
            return saleService.createSale(saleDto);
    }

    @GetMapping
    public List<SaleDto> getAllSales() throws CustomValidationException {
        return saleService.getAllSales();
    }

    @GetMapping("/analytics")
    public SaleAnalyticsDto getSalesAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws CustomValidationException {
        return saleService.getSalesAnalytics(startDate, endDate);
    }
    @GetMapping("/analytics/branch/{branchId}")
    public ResponseEntity<SaleAnalyticsResponse> getBranchSalesAnalytics(
            @PathVariable String branchId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate)throws CustomValidationException {
        return ResponseEntity.ok(saleService.getBranchSalesAnalytics(branchId, startDate, endDate));
    }

    @GetMapping("/analytics/product/{productId}")
    public ResponseEntity<SaleAnalyticsResponse> getProductSalesAnalytics(
            @PathVariable String productId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate)  throws CustomValidationException{
        return ResponseEntity.ok(saleService.getProductSalesAnalytics(productId, startDate, endDate));
    }
    @GetMapping("/filter")
    public SaleAnalyticsResponse filterSalesAnalytics(
            @RequestParam(required = false) String branchId,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) Double minRevenue,
            @RequestParam(required = false) Double maxRevenue,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
            throws CustomValidationException {

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        List<SaleAnalyticsResponse> trend = saleService.getDailySalesTrend(startDate, endDate);
        List<SaleAnalyticsResponse> filtered = trend.stream()
                .map(response -> {
                    SaleAnalyticsResponse res = new SaleAnalyticsResponse();

                    res.setSales(response.getSales().stream()
                            .filter(sale -> {
                                Double total = sale.getTotalAmount();
                                if (total == null) total = 0.0;

                                boolean branchMatch = (branchId == null || branchId.equals(sale.getBranchId()));
                                boolean productMatch = (productId == null || productId.equals(sale.getProductId()));
                                boolean minMatch = (minRevenue == null || total >= minRevenue);
                                boolean maxMatch = (maxRevenue == null || total <= maxRevenue);

                                return branchMatch && productMatch && minMatch && maxMatch;
                            })
                            .toList());

                    res.setTotalCount(res.getSales().size());
                    res.setTotalRevenue(res.getSales().stream()
                            .mapToDouble(s -> s.getTotalAmount() != null ? s.getTotalAmount() : 0)
                            .sum());

                    return res;
                })
                .filter(r -> !r.getSales().isEmpty())
                .toList();

        SaleAnalyticsResponse overall = new SaleAnalyticsResponse();
        overall.setSales(filtered.stream()
                .flatMap(r -> r.getSales().stream())
                .toList());
        overall.setTotalCount(overall.getSales().size());
        overall.setTotalRevenue(overall.getSales().stream()
                .mapToDouble(s -> s.getTotalAmount() != null ? s.getTotalAmount() : 0)
                .sum());

        return overall;
    }
    @PostMapping("/sales/verify-otp")
    public ResponseEntity<String> verifySaleOtp(
            @RequestParam String saleId,
            @RequestParam String otp) throws CustomValidationException {

        boolean verified = saleService.verifySaleOtp(saleId, otp);
        if (verified) {
            return ResponseEntity.ok("OTP verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }


}

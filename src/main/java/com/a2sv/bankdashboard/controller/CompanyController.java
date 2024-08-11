package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.CompanyRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.CompanyResponse;
import com.a2sv.bankdashboard.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies(@RequestParam int page, @RequestParam int size) {
        List<CompanyResponse> companies = companyService.findAll(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Companies retrieved successfully", companies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable String id) {
        CompanyResponse company = companyService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company retrieved successfully", company));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        CompanyResponse company = companyService.save(companyRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company created successfully", company));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(@PathVariable String id, @Valid @RequestBody CompanyRequest companyRequest) {
        CompanyResponse company = companyService.update(id, companyRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company updated successfully", company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable String id) {
        companyService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company deleted successfully", null));
    }

    @GetMapping("/trending-companies")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getTrendingCompanies() {
        List<CompanyResponse> companies = companyService.trendingStock();
        return ResponseEntity.ok(new ApiResponse<>(true, "Trending companies retrieved successfully", companies));
    }
}
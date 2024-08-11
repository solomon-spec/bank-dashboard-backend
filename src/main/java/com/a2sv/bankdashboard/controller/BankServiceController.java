package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.BankServiceRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.BankServiceResponse;
import com.a2sv.bankdashboard.service.BankServiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-services")
public class BankServiceController {

    private final BankServiceService bankServiceService;

    public BankServiceController(BankServiceService bankServiceService) {
        this.bankServiceService = bankServiceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BankServiceResponse>> createService(@Valid @RequestBody BankServiceRequest request) {
        ApiResponse<BankServiceResponse> response = bankServiceService.createService(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankServiceResponse>> updateService(@PathVariable String id,@Valid @RequestBody BankServiceRequest request) {
        ApiResponse<BankServiceResponse> response = bankServiceService.updateService(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable String id) {
        ApiResponse<Void> response = bankServiceService.deleteService(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankServiceResponse>> getService(@PathVariable String id) {
        ApiResponse<BankServiceResponse> response = bankServiceService.getService(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BankServiceResponse>>> getAllServices(@RequestParam int page, @RequestParam int size) {
        ApiResponse<List<BankServiceResponse>> response = bankServiceService.getAllServices(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BankServiceResponse>>> searchServices(@RequestParam String query) {
        ApiResponse<List<BankServiceResponse>> response = bankServiceService.searchServices(query);
        return ResponseEntity.ok(response);
    }
}
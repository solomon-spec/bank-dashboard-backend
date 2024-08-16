package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.ActiveLoanRequest;
import com.a2sv.bankdashboard.dto.response.ActiveLoanResponse;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.TotalLoanDetail;
import com.a2sv.bankdashboard.service.ActiveLoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/active-loans")
public class ActiveLoanController {

    private final ActiveLoanService activeLoanService;

    @Autowired
    public ActiveLoanController(ActiveLoanService activeLoanService) {
        this.activeLoanService = activeLoanService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ActiveLoanResponse>>> getAllActiveLoans() {
        List<ActiveLoanResponse> loans = activeLoanService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "All active loans retrieved successfully", loans));
    }
    @GetMapping("/my-loans")
    public ResponseEntity<ApiResponse<List<ActiveLoanResponse>>> getMyActiveLoans() {
        List<ActiveLoanResponse> loans = activeLoanService.findUsersActiveLoans();
        return ResponseEntity.ok(new ApiResponse<>(true, "User's active loans retrieved successfully", loans));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActiveLoanResponse>> getActiveLoanById(@PathVariable String id) {
        ActiveLoanResponse loan = activeLoanService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Active loan retrieved successfully", loan));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ActiveLoanResponse>> requestLoan(@Valid @RequestBody ActiveLoanRequest activeLoanRequest) {
        ActiveLoanResponse loan = activeLoanService.save(activeLoanRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Loan requested successfully", loan));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ActiveLoanResponse>> approveLoan(@PathVariable String id) {
        ActiveLoanResponse loan = activeLoanService.approveLoan(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Loan approved successfully", loan));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectLoan(@PathVariable String id) {
        activeLoanService.rejectLoan(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Loan rejected successfully", null));
    }
    @GetMapping("/detail-data")
    public ResponseEntity<ApiResponse<TotalLoanDetail>> getTotalLoanDetail() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Loan rejected successfully", activeLoanService.getTotalLoanDetail()));
    }


}
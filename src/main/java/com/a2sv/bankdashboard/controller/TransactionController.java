package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.TransactionRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.TransactionResponse;
import com.a2sv.bankdashboard.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllUserTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<TransactionResponse> transactions = transactionService.getAllUserTransactions(page, size);
        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(true, "Transactions retrieved successfully", transactions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable Long id) {
        TransactionResponse transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            ApiResponse<TransactionResponse> response = new ApiResponse<>(false, "Transaction not found", null);
            return ResponseEntity.notFound().build();
        }
        ApiResponse<TransactionResponse> response = new ApiResponse<>(true, "Transaction retrieved successfully", transaction);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> saveTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse savedTransaction = transactionService.saveTransaction(transactionRequest);
        ApiResponse<TransactionResponse> response = new ApiResponse<>(true, "Transaction saved successfully", savedTransaction);
        return ResponseEntity.ok(response);
    }
}
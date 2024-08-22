package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.TransactionDepositRequest;
import com.a2sv.bankdashboard.dto.request.TransactionRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.PagedResponse;
import com.a2sv.bankdashboard.dto.response.PublicUserResponse;
import com.a2sv.bankdashboard.dto.response.TransactionResponse;
import com.a2sv.bankdashboard.model.TimeValue;
import com.a2sv.bankdashboard.service.TransactionService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getAllUserTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<TransactionResponse> transactions = transactionService.getAllUserTransactions(page, size);
        ApiResponse<PagedResponse<TransactionResponse>> response = new ApiResponse<>(true, "Transactions retrieved successfully", transactions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
//            ApiResponse<TransactionResponse> response = new ApiResponse<>(false, "Transaction not found", null);
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

    @GetMapping("/incomes")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getIncomes(@RequestParam int page, @RequestParam int size) {
        PagedResponse<TransactionResponse> incomes = transactionService.getIncomes(page, size);
        ApiResponse<PagedResponse<TransactionResponse>> response = new ApiResponse<>(true, "Incomes fetched successfully", incomes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getExpenses(@RequestParam int page, @RequestParam int size) {
        PagedResponse<TransactionResponse> expenses = transactionService.getExpenses(page, size);
        ApiResponse<PagedResponse<TransactionResponse>> response = new ApiResponse<>(true, "Expenses fetched successfully", expenses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/quick-transfers")
    public ResponseEntity<ApiResponse<List<PublicUserResponse>>> latestTransfers(@RequestParam int number) {
        List<PublicUserResponse> transfers = transactionService.latestTransfers(number);
        ApiResponse<List<PublicUserResponse>> response = new ApiResponse<>(true, "Latest transfers fetched successfully", transfers);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@Valid @RequestBody TransactionDepositRequest transactionRequest) {
        TransactionResponse depositTransaction = transactionService.deposit(transactionRequest);
        ApiResponse<TransactionResponse> response = new ApiResponse<>(true, "Deposit successful", depositTransaction);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/balance-history")
    public ResponseEntity<ApiResponse<List<TimeValue>>> getBalanceHistory() {
        List<TimeValue> balanceHistory = transactionService.getBalanceHistory();
        ApiResponse<List<TimeValue>> response = new ApiResponse<>(true, "Balance history retrieved successfully", balanceHistory);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/random-balance-history")
    public ResponseEntity<ApiResponse<List<TimeValue>>> generateRandomBalanceHistory(@RequestParam int monthsBeforeFirstTransaction) {
        List<TimeValue> randomBalanceHistory = transactionService.generateRandomBalanceHistory(monthsBeforeFirstTransaction);
        ApiResponse<List<TimeValue>> response = new ApiResponse<>(true, "Random balance history generated successfully", randomBalanceHistory);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/expenses-summary")
    public ResponseEntity<ApiResponse<List<TimeValue>>> getExpensesSummary(
            @RequestParam LocalDate startDate,
            @ApiParam(value = "Type of summary: 'daily' or 'monthly'", required = true, allowableValues = "daily, monthly")
            @RequestParam String type) {
        List<TimeValue> expenses;
        if ("daily".equalsIgnoreCase(type)) {
            expenses = transactionService.getDailyExpense(startDate);
        } else if ("monthly".equalsIgnoreCase(type)) {
            expenses = transactionService.getMonthlyExpense(startDate);
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid type parameter", null));
        }
        ApiResponse<List<TimeValue>> response = new ApiResponse<>(true, "Expenses summary retrieved successfully", expenses);
        return ResponseEntity.ok(response);
    }

}
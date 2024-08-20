package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.TransactionDepositRequest;
import com.a2sv.bankdashboard.dto.request.TransactionRequest;
import com.a2sv.bankdashboard.dto.response.PublicUserResponse;
import com.a2sv.bankdashboard.dto.response.TransactionResponse;
import com.a2sv.bankdashboard.exception.InsufficientBalanceException;
import com.a2sv.bankdashboard.exception.ResourceNotFoundException;
import com.a2sv.bankdashboard.model.TimeValue;
import com.a2sv.bankdashboard.model.Transaction;
import com.a2sv.bankdashboard.model.TransactionType;
import com.a2sv.bankdashboard.model.User;
import com.a2sv.bankdashboard.repository.TransactionRepository;
import com.a2sv.bankdashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }
    public List<TransactionResponse> getAllUserTransactions(int page, int size) {
        User currentUser = authenticationService.getCurrentUser();
        Page<Transaction> transactionsPage = transactionRepository.findBySenderOrReceiver(currentUser,currentUser, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")));

        return transactionsPage.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::convertToResponse)
                .orElse(null);
    }

    @Transactional
    public TransactionResponse saveTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = convertToEntity(transactionRequest);
        if (transaction.getSender().getAccountBalance() < transactionRequest.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance for the transaction");
        }
        Transaction savedTransaction = transactionRepository.save(transaction);
        transaction.getSender().setAccountBalance(transaction.getSender().getAccountBalance() - transactionRequest.getAmount());
        if(transactionRequest.getType() == TransactionType.transfer){
            transaction.getReceiver().setAccountBalance(transaction.getReceiver().getAccountBalance() + transactionRequest.getAmount());
            userRepository.save(transaction.getReceiver());
        }
        userRepository.save(transaction.getSender());

        return convertToResponse(savedTransaction);
    }

    private Transaction convertToEntity(TransactionRequest transactionRequest) {

        // Assuming User entities for sender and receiver are fetched from the database
        User sender = authenticationService.getCurrentUser();
        User receiver = null;
        if(transactionRequest.getType() == TransactionType.transfer){
            receiver = userRepository.findByUsername(transactionRequest.getReceiverUserName())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        else{
            transactionRequest.setReceiverUserName(null);
        }

        return new Transaction(
                null,
                sender,
                transactionRequest.getType(),
                transactionRequest.getDescription(),
                LocalDate.now(),
                transactionRequest.getAmount(),
                receiver
        );
    }

    private TransactionResponse convertToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getType(),
                transaction.getSender().getUsername(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.getReceiver() != null ? transaction.getReceiver().getUsername() : null
        );
    }

    public List<TransactionResponse> getIncomes(int page, int size){
        User currentUser = authenticationService.getCurrentUser();
        List<Transaction> transactions1 = transactionRepository.findByReceiver(currentUser);
        List<Transaction> transactions2 = transactionRepository.findBySenderAndType(currentUser, TransactionType.deposit);
        transactions1.addAll(transactions2);
        transactions1.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        int start = Math.min(page * size, transactions1.size());
        int end = Math.min((page + 1) * size, transactions1.size());
        List<Transaction> paginatedTransactions = transactions1.subList(start, end);

        return paginatedTransactions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    public List<TransactionResponse> getExpenses(int page, int size){
        User currentUser = authenticationService.getCurrentUser();
        Page<Transaction> transactionsPage = transactionRepository.findBySenderAndTypeNot(currentUser, TransactionType.deposit, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")));

        return transactionsPage.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    public List<PublicUserResponse> latestTransfers(int number){
        User currentUser = authenticationService.getCurrentUser();
        Page<Transaction> transactionsPage = transactionRepository.findByTypeAndSenderOrReceiver(TransactionType.transfer,currentUser, currentUser, PageRequest.of(0, number, Sort.by(Sort.Direction.DESC, "date")));
        Set<String> uniqueUsernames = new HashSet<>();
        return transactionsPage.stream()
                .map(transaction -> {
                    User otherUser = !transaction.getReceiver().equals(currentUser) ? transaction.getReceiver(): transaction.getSender() ;
                    return new PublicUserResponse(
                            otherUser.getId(),
                            otherUser.getName(),
                            otherUser.getUsername(),
                            otherUser.getCity(),
                            otherUser.getCountry(),
                            otherUser.getEmail()
                );})
                .filter(user -> !Objects.equals(user.getUsername(), currentUser.getUsername()) && uniqueUsernames.add(user.getUsername()))
                .collect(Collectors.toList());
    }
    @Transactional
    public TransactionResponse deposit(TransactionDepositRequest transactionRequest) {
        User currentUser = authenticationService.getCurrentUser();
        if (transactionRequest.getAmount() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Transaction transaction = new Transaction(
                null,
                currentUser,
                TransactionType.deposit,
                transactionRequest.getDescription(),
                LocalDate.now(),
                transactionRequest.getAmount(),
                null
        );

        currentUser.setAccountBalance(currentUser.getAccountBalance() + transactionRequest.getAmount());
        userRepository.save(currentUser);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToResponse(savedTransaction);
    }

    public List<TimeValue> getBalanceHistory() {
        User currentUser = authenticationService.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findBySenderOrReceiver(currentUser, currentUser);

        Map<YearMonth, Double> balanceHistory = new TreeMap<>();
        double currentBalance = currentUser.getAccountBalance();

        for (Transaction transaction : transactions) {
            YearMonth yearMonth = YearMonth.from(transaction.getDate());
            balanceHistory.putIfAbsent(yearMonth, currentBalance);
            if (transaction.getSender().equals(currentUser)) {
                currentBalance -= transaction.getAmount();
            } else {
                currentBalance += transaction.getAmount();
            }
            balanceHistory.put(yearMonth, currentBalance);
        }

        return balanceHistory.entrySet().stream()
                .map(entry -> new TimeValue(entry.getKey().toString(), entry.getValue()))
                .collect(Collectors.toList());
    }
    public List<TimeValue> generateRandomBalanceHistory(int monthsBeforeFirstTransaction) {
        User currentUser = authenticationService.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findBySenderOrReceiver(currentUser, currentUser);

        LocalDate firstTransactionDate = transactions.isEmpty() ? LocalDate.now() : transactions.get(0).getDate();
        YearMonth firstTransactionYearMonth = YearMonth.from(firstTransactionDate);
        Random random = new Random();
        double initialBalance = 1000 + (5000 - 1000) * random.nextDouble();

        Map<YearMonth, Double> randomBalanceHistory = new TreeMap<>();
        for (int i = monthsBeforeFirstTransaction; i > 0; i--) {
            YearMonth yearMonth = firstTransactionYearMonth.minusMonths(i);
            randomBalanceHistory.put(yearMonth, initialBalance);
            initialBalance = 1000 + (5000 - 1000) * random.nextDouble();
        }

        return randomBalanceHistory.entrySet().stream()
                .map(entry -> new TimeValue(entry.getKey().toString(), entry.getValue()))
                .toList();
    }

}

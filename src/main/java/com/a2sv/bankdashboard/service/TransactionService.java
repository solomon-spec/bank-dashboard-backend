package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.TransactionRequest;
import com.a2sv.bankdashboard.dto.response.PublicUserResponse;
import com.a2sv.bankdashboard.dto.response.TransactionResponse;
import com.a2sv.bankdashboard.exception.InsufficientBalanceException;
import com.a2sv.bankdashboard.exception.ResourceNotFoundException;
import com.a2sv.bankdashboard.model.Transaction;
import com.a2sv.bankdashboard.model.TransactionType;
import com.a2sv.bankdashboard.model.User;
import com.a2sv.bankdashboard.repository.TransactionRepository;
import com.a2sv.bankdashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Page<Transaction> transactionsPage = transactionRepository.findBySenderOrReceiver(currentUser,currentUser, PageRequest.of(page, size));

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

        return new Transaction(
                null,
                sender,
                transactionRequest.getType(),
                transactionRequest.getDescription(),
                transactionRequest.getDate(),
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
                transaction.getReceiver().getUsername()
        );
    }

    private List<TransactionResponse> getIncomes(int page, int size){
        User currentUser = authenticationService.getCurrentUser();
        Page<Transaction> transactionsPage = transactionRepository.findByReceiver(currentUser,currentUser, PageRequest.of(page, size));

        return transactionsPage.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    private List<TransactionResponse> getExpenses(int page, int size){
        User currentUser = authenticationService.getCurrentUser();
        Page<Transaction> transactionsPage = transactionRepository.findBySender(currentUser, PageRequest.of(page, size));

        return transactionsPage.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    private List<PublicUserResponse> latestTransfers(int number){
        User currentUser = authenticationService.getCurrentUser();
        Page<Transaction> transactionsPage = transactionRepository.findByTypeAndSenderOrReceiver(TransactionType.transfer,currentUser, currentUser, PageRequest.of(0, number));

        return transactionsPage.stream()
                .map(transaction -> new PublicUserResponse(
                        transaction.getReceiver().getId(),
                        transaction.getReceiver().getName(),
                        transaction.getReceiver().getUsername(),
                        transaction.getReceiver().getCity(),
                        transaction.getReceiver().getCountry(),
                        transaction.getReceiver().getEmail()
                ))
                .collect(Collectors.toList());
    }

}

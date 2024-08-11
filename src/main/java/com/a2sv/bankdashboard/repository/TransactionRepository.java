package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Transaction;
import com.a2sv.bankdashboard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> findBySender(User sender, Pageable pageable);
}

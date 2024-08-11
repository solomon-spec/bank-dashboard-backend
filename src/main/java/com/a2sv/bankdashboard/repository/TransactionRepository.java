package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Transaction;
import com.a2sv.bankdashboard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, Long> {
    Page<Transaction> findBySender(User sender, Pageable pageable);
}

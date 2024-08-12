package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Transaction;
import com.a2sv.bankdashboard.model.TransactionType;
import com.a2sv.bankdashboard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Page<Transaction> findBySender(User sender, Pageable pageable);
    Page<Transaction> findByReceiver(User receiver, Pageable pageable);
    Page<Transaction> findBySenderOrReceiver(User sender, User receiver, Pageable pageable);
    Page<Transaction> findByTypeAndSenderOrReceiver(TransactionType type, User sender, User receiver, Pageable pageable);
}

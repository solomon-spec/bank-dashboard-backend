package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Transaction;
import com.a2sv.bankdashboard.model.TransactionType;
import com.a2sv.bankdashboard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Page<Transaction> findBySenderOrReceiver(User sender, User receiver, Pageable pageable);
    Page<Transaction> findByTypeAndSenderOrReceiver(TransactionType type, User sender, User receiver, Pageable pageable);
    List<Transaction> findBySenderOrReceiver(User sender, User receiver);
    Page<Transaction> findBySenderAndTypeNot(User sender, TransactionType type, Pageable pageable);
    List<Transaction> findByReceiver(User receiver);
    List<Transaction> findBySenderAndType(User sender, TransactionType type);

}

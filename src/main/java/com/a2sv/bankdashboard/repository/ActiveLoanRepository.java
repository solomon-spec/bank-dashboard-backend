package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.ActiveLoan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActiveLoanRepository extends MongoRepository<ActiveLoan, Long> {
    List<ActiveLoan> findByUserUsername(String user_username);
}
package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.ActiveLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ActiveLoanRepository extends MongoRepository<ActiveLoan, String> {
    Page<ActiveLoan> findByUserId(String id, Pageable pageable);


}
package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.dto.response.TotalLoanDetail;
import com.a2sv.bankdashboard.model.ActiveLoan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ActiveLoanRepository extends MongoRepository<ActiveLoan, String> {
    List<ActiveLoan> findByUserId(String id);

    @Query(value = "{}", fields = "{ 'type': 1, 'loanAmount': 1 }")
    List<ActiveLoan> findAllLoanDetails();


}
package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.ActiveLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActiveLoanRepository extends JpaRepository<ActiveLoan, Long> {
    List<ActiveLoan> findByUserUsername(String user_username);
}
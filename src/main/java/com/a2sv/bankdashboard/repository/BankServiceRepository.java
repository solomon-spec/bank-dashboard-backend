package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.BankService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankServiceRepository extends JpaRepository<BankService, Long> {
    List<BankService> findByNameContaining(String name);
}
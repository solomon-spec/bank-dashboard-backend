package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.BankService;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BankServiceRepository extends MongoRepository<BankService, Long> {
    List<BankService> findByNameContaining(String name);
}
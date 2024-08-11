package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
}
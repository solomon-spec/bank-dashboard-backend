package com.a2sv.bankdashboard.repository;

import com.a2sv.bankdashboard.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(value = "SELECT * FROM Company ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Company> findRandomCompanies();
}
package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.CompanyRequest;
import com.a2sv.bankdashboard.dto.response.CompanyResponse;
import com.a2sv.bankdashboard.model.Company;
import com.a2sv.bankdashboard.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse findById(String id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return convertToDto(company);
    }

    public CompanyResponse save(@Valid CompanyRequest companyRequest) {
        Company company = convertToEntity(companyRequest);
        Company savedCompany = companyRepository.save(company);
        return convertToDto(savedCompany);
    }

    public CompanyResponse update(String id, @Valid CompanyRequest companyRequest) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompanyName(companyRequest.getCompanyName());
        company.setType(companyRequest.getType());
        company.setIcon(companyRequest.getIcon());
        Company updatedCompany = companyRepository.save(company);
        return convertToDto(updatedCompany);
    }

    public void deleteById(String id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company not found");
        }
        companyRepository.deleteById(id);
    }

    public List<CompanyResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findAll(pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Company convertToEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        company.setCompanyName(companyRequest.getCompanyName());
        company.setType(companyRequest.getType());
        company.setIcon(companyRequest.getIcon());
        return company;
    }

    public CompanyResponse convertToDto(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(company.getId());
        companyResponse.setCompanyName(company.getCompanyName());
        companyResponse.setType(company.getType());
        companyResponse.setIcon(company.getIcon());
        return companyResponse;
    }
    public List<CompanyResponse> trendingStock(){
        return companyRepository.findAll().stream().map(this::convertToDto).toList();
    }
}
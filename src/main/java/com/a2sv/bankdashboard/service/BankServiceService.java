package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.BankServiceRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.BankServiceResponse;
import com.a2sv.bankdashboard.model.BankService;
import com.a2sv.bankdashboard.repository.BankServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceService {

    private final BankServiceRepository repository;

    public BankServiceService(BankServiceRepository repository) {
        this.repository = repository;
    }

    public ApiResponse<BankServiceResponse> createService(BankServiceRequest request) {
        BankService bankService = new BankService();
        bankService.setName(request.getName());
        bankService.setDetails(request.getDetails());
        bankService.setNumberOfUsers(request.getNumberOfUsers());
        bankService.setStatus(request.getStatus());
        bankService.setType(request.getType());
        bankService.setIcon(request.getIcon());

        bankService = repository.save(bankService);

        BankServiceResponse response = new BankServiceResponse();
        response.setId(bankService.getId());
        response.setName(bankService.getName());
        response.setDetails(bankService.getDetails());
        response.setNumberOfUsers(bankService.getNumberOfUsers());
        response.setStatus(bankService.getStatus());
        response.setType(bankService.getType());
        response.setIcon(bankService.getIcon());

        return new ApiResponse<>(true, "Bank service created successfully", response);
    }

    public ApiResponse<BankServiceResponse> updateService(String id, BankServiceRequest request) {
        BankService bankService = repository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        bankService.setName(request.getName());
        bankService.setDetails(request.getDetails());
        bankService.setNumberOfUsers(request.getNumberOfUsers());
        bankService.setStatus(request.getStatus());
        bankService.setType(request.getType());
        bankService.setIcon(request.getIcon());

        bankService = repository.save(bankService);

        BankServiceResponse response = new BankServiceResponse();
        response.setId(bankService.getId());
        response.setName(bankService.getName());
        response.setDetails(bankService.getDetails());
        response.setNumberOfUsers(bankService.getNumberOfUsers());
        response.setStatus(bankService.getStatus());
        response.setType(bankService.getType());
        response.setIcon(bankService.getIcon());

        return new ApiResponse<>(true, "Bank service updated successfully", response);
    }

    public ApiResponse<Void> deleteService(String id) {
        repository.deleteById(id);
        return new ApiResponse<>(true, "Bank service deleted successfully", null);
    }

    public ApiResponse<BankServiceResponse> getService(String id) {
        BankService bankService = repository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        BankServiceResponse response = new BankServiceResponse();
        response.setId(bankService.getId());
        response.setName(bankService.getName());
        response.setDetails(bankService.getDetails());
        response.setNumberOfUsers(bankService.getNumberOfUsers());
        response.setStatus(bankService.getStatus());
        response.setType(bankService.getType());
        response.setIcon(bankService.getIcon());

        return new ApiResponse<>(true, "Bank service retrieved successfully", response);
    }

    public ApiResponse<List<BankServiceResponse>> getAllServices(int page, int size) {
        Page<BankService> services = repository.findAll(PageRequest.of(page, size));
        List<BankServiceResponse> responses = services.stream().map(service -> {
            BankServiceResponse response = new BankServiceResponse();
            response.setId(service.getId());
            response.setName(service.getName());
            response.setDetails(service.getDetails());
            response.setNumberOfUsers(service.getNumberOfUsers());
            response.setStatus(service.getStatus());
            response.setType(service.getType());
            response.setIcon(service.getIcon());
            return response;
        }).collect(Collectors.toList());

        return new ApiResponse<>(true, "Bank services retrieved successfully", responses);
    }

    public ApiResponse<List<BankServiceResponse>> searchServices(String query) {
        List<BankService> services = repository.findByNameContaining(query);
        List<BankServiceResponse> responses = services.stream().map(service -> {
            BankServiceResponse response = new BankServiceResponse();
            response.setId(service.getId());
            response.setName(service.getName());
            response.setDetails(service.getDetails());
            response.setNumberOfUsers(service.getNumberOfUsers());
            response.setStatus(service.getStatus());
            response.setType(service.getType());
            response.setIcon(service.getIcon());
            return response;
        }).collect(Collectors.toList());

        return new ApiResponse<>(true, "Bank services retrieved successfully", responses);
    }
}
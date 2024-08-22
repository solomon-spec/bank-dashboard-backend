package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.ActiveLoanRequest;
import com.a2sv.bankdashboard.dto.response.ActiveLoanResponse;
import com.a2sv.bankdashboard.dto.response.PagedResponse;
import com.a2sv.bankdashboard.dto.response.TotalLoanDetail;
import com.a2sv.bankdashboard.exception.ResourceNotFoundException;
import com.a2sv.bankdashboard.model.ActiveLoan;
import com.a2sv.bankdashboard.model.ActiveLoanType;
import com.a2sv.bankdashboard.model.ActiveLoneStatus;
import com.a2sv.bankdashboard.model.User;
import com.a2sv.bankdashboard.repository.ActiveLoanRepository;
import com.a2sv.bankdashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

@Service
public class ActiveLoanService {

    private final ActiveLoanRepository activeLoanRepository;
    private final UserRepository userRepository;

    @Autowired
    public ActiveLoanService(ActiveLoanRepository activeLoanRepository, UserRepository userRepository) {
        this.activeLoanRepository = activeLoanRepository;
        this.userRepository = userRepository;
    }

    public ActiveLoanResponse convertToDto(ActiveLoan activeLoan){
        return new ActiveLoanResponse(
                activeLoan.getSerialNumber(),
                activeLoan.getLoanAmount(),
                activeLoan.getAmountLeftToRepay(),
                activeLoan.getDuration(),
                activeLoan.getInterestRate(),
                activeLoan.getInstallment(),
                activeLoan.getType(),
                activeLoan.getActiveLoneStatus(),
                activeLoan.getUser().getId()

        );
    }
    public PagedResponse<ActiveLoanResponse> findAll(int page, int size) {
        Page<ActiveLoan> activeLoansPage = activeLoanRepository.findAll(PageRequest.of(page, size));
        List<ActiveLoanResponse> activeLoanResponses = activeLoansPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(activeLoanResponses, activeLoansPage.getTotalPages());
    }
    public PagedResponse<ActiveLoanResponse> findUsersActiveLoans(int page, int size) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Page<ActiveLoan> activeLoansPage = activeLoanRepository.findByUserId(user.getId(), PageRequest.of(page, size));
        List<ActiveLoanResponse> activeLoanResponses = activeLoansPage.stream()
                .map(this::convertToDto)
                .toList();

        return new PagedResponse<>(activeLoanResponses, activeLoansPage.getTotalPages());
    }


    public ActiveLoanResponse findById(String id) {
        return convertToDto(activeLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active loan not found")));
    }


    public ActiveLoanResponse save(ActiveLoanRequest activeLoanRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ActiveLoan activeLoan = new ActiveLoan();
        activeLoan.setLoanAmount(activeLoanRequest.getLoanAmount());
        activeLoan.setDuration(activeLoanRequest.getDuration());
        activeLoan.setInterestRate(activeLoanRequest.getInterestRate());
        activeLoan.setType(activeLoanRequest.getType());
        activeLoan.setActiveLoneStatus(ActiveLoneStatus.pending);
        activeLoan.setType(activeLoanRequest.getType());
        activeLoan.setUser(userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        ));
        activeLoan.setAmountLeftToRepay(activeLoan.getLoanAmount());
        int numberOfPayment = activeLoanRequest.getDuration() * 12;
        double principal = activeLoan.getLoanAmount();
        double monthlyInterestRate = (activeLoan.getInterestRate() / 12);
        double EMI = principal * ((monthlyInterestRate * pow(1 + monthlyInterestRate,numberOfPayment)) /
                (pow(1 + monthlyInterestRate, numberOfPayment) - 1));
        activeLoan.setInstallment(EMI);

        return convertToDto(activeLoanRepository.save(activeLoan));
    }




    public ActiveLoanResponse approveLoan(String id) {
        ActiveLoan loan = activeLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active loan not found"));

        loan.setActiveLoneStatus(ActiveLoneStatus.approved);

        userRepository.findById(loan.getUser().getId()).ifPresentOrElse(user -> {
            user.setAccountBalance(user.getAccountBalance() + loan.getLoanAmount());
            userRepository.save(user);
        }, () -> {
            throw new ResourceNotFoundException("User not found");
        });

        return convertToDto(activeLoanRepository.save(loan));
    }

    public void rejectLoan(String id) {
        ActiveLoan loan = activeLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active loan not found"));

        loan.setActiveLoneStatus(ActiveLoneStatus.rejected);

        userRepository.findById(loan.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        activeLoanRepository.save(loan);
    }
    public ActiveLoanResponse repay(String id) {
        ActiveLoan loan = activeLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active loan not found"));
        User user = loan.getUser();
        double repaymentAmount = loan.getAmountLeftToRepay();

        if (user.getAccountBalance() < repaymentAmount) {
            throw new RuntimeException("Insufficient funds to repay the loan");
        }

        user.setAccountBalance(user.getAccountBalance() - repaymentAmount);
        loan.setAmountLeftToRepay(0);
        loan.setActiveLoneStatus(ActiveLoneStatus.repaid);

        userRepository.save(user);
        return convertToDto(activeLoanRepository.save(loan));
    }
    private double calculateTotalLoanAmount(List<ActiveLoan> loans) {
        return loans.stream().mapToDouble(ActiveLoan::getLoanAmount).sum();
    }
    public TotalLoanDetail getTotalLoanDetail() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        double personalLoanTotal = calculateTotalLoanAmount(activeLoanRepository.findByUserId(user.getId(), Pageable.unpaged()).stream()
                .filter(loan -> loan.getType().equals(ActiveLoanType.personal)).toList());
        double businessLoanTotal = calculateTotalLoanAmount(activeLoanRepository.findByUserId(user.getId(), Pageable.unpaged()).stream()
                .filter(loan -> loan.getType().equals(ActiveLoanType.business)).toList());
        double corporateLoanTotal = calculateTotalLoanAmount(activeLoanRepository.findByUserId(user.getId(), Pageable.unpaged()).stream()
                .filter(loan -> loan.getType().equals(ActiveLoanType.corporate)).toList());

        TotalLoanDetail totalLoanDetail = new TotalLoanDetail();
        totalLoanDetail.setPersonalLoan(personalLoanTotal);
        totalLoanDetail.setBusinessLoan(businessLoanTotal);
        totalLoanDetail.setCorporateLoan(corporateLoanTotal);

        return totalLoanDetail;
    }

}
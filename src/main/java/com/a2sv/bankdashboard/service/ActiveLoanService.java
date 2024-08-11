package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.ActiveLoanRequest;
import com.a2sv.bankdashboard.dto.response.ActiveLoanResponse;
import com.a2sv.bankdashboard.model.ActiveLoan;
import com.a2sv.bankdashboard.model.ActiveLoneStatus;
import com.a2sv.bankdashboard.repository.ActiveLoanRepository;
import com.a2sv.bankdashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ActiveLoanResponse> findAll() {
        return activeLoanRepository.findAll().stream().map(this::convertToDto).toList();
    }
    public List<ActiveLoanResponse> findUsersActiveLoans(){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return activeLoanRepository.findByUserUsername(currentUsername).stream().map(this::convertToDto).toList();
    }


    public ActiveLoanResponse findById(Long id) {
        return convertToDto(activeLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Active loan not found")));
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
                () -> new UsernameNotFoundException("User not found")
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




    public ActiveLoanResponse approveLoan(Long id) {
        ActiveLoan loan = activeLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Active loan not found"));

        loan.setActiveLoneStatus(ActiveLoneStatus.approved);

        userRepository.findById(loan.getUser().getId()).ifPresentOrElse(user -> {
            user.setAccountCash(user.getAccountCash() + loan.getLoanAmount());
            userRepository.save(user);
        }, () -> {
            throw new RuntimeException("User not found");
        });

        return convertToDto(activeLoanRepository.save(loan));
    }

    public void rejectLoan(Long id) {
        ActiveLoan loan = activeLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Active loan not found"));

        loan.setActiveLoneStatus(ActiveLoneStatus.rejected);

        userRepository.findById(loan.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));

        activeLoanRepository.save(loan);
    }
}
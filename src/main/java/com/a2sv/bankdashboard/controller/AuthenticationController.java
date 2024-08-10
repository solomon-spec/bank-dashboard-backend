package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.*;
import com.a2sv.bankdashboard.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse< AuthenticationResponse>> register(
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public
    ResponseEntity<ApiResponse<AuthenticationResponse>> login(
           @Valid @RequestBody UserLogin request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<ApiResponse<String>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/change_password")
    public ResponseEntity<ApiResponse<Void>> changePassword(ChangePassword changePassword){
        return ResponseEntity.ok(authService.changePassword(changePassword));
    }
}
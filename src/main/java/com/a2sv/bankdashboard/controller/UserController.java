package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.UserRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.UserResponse;
import com.a2sv.bankdashboard.model.Preference;
import com.a2sv.bankdashboard.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDetailsServiceImp userDetailsService;

    public UserController(UserDetailsServiceImp userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@Valid @RequestBody UserRequest request) {
        ApiResponse<UserResponse> response = userDetailsService.update(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable String username) {
        ApiResponse<?> response = userDetailsService.getUser(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<?>> getCurrentUser() {
        ApiResponse<?> response = userDetailsService.getCurrentUser();
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update-preference")
    public ResponseEntity<ApiResponse<UserResponse>> updatePreference(@Valid @RequestBody Preference preference) {
        ApiResponse<UserResponse> response = userDetailsService.savePreference(preference);
        return ResponseEntity.ok(response);
    }
}
package com.a2sv.bankdashboard.service;

import com.a2sv.bankdashboard.dto.request.ChangePassword;
import com.a2sv.bankdashboard.dto.request.UserLogin;
import com.a2sv.bankdashboard.dto.request.UserRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.AuthenticationResponse;
import com.a2sv.bankdashboard.dto.response.UserResponse;
import com.a2sv.bankdashboard.model.Role;
import com.a2sv.bankdashboard.model.Token;
import com.a2sv.bankdashboard.model.User;
import com.a2sv.bankdashboard.repository.TokenRepository;
import com.a2sv.bankdashboard.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsServiceImp;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService, UserDetailsServiceImp userDetailsServiceImp,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public ApiResponse<AuthenticationResponse<UserResponse>> register(UserRequest request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new ApiResponse<>(false, "User already exists", null);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPermanentAddress(request.getPermanentAddress());
        user.setPostalCode(request.getPostalCode());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPresentAddress(request.getPresentAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setProfilePicture(request.getProfilePicture());
        user.setAccountCash(0.0);
        user.setPreference(request.getPreference());

        user.setRole(Role.USER);

        user = repository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);


        // Create the authentication response
        AuthenticationResponse<UserResponse> authResponse = new AuthenticationResponse<UserResponse>(
                accessToken, refreshToken, userDetailsServiceImp.convertToUserDto(user) );

        // Return the API response
        return new ApiResponse<>(true, "User registered successfully", authResponse);
    }

    public ApiResponse<AuthenticationResponse<Void>> authenticate(UserLogin request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUserName()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        AuthenticationResponse<Void> authResponse = new AuthenticationResponse<>(accessToken, refreshToken, null);
        return new ApiResponse<>(true, "User authenticated successfully", authResponse);

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public ApiResponse<String> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponse<>(false, "Invalid authorization header", null);
        }


        String token = authHeader.substring(7);

        // extract username from token
        String username = jwtService.extractUsername(token);

        // check if the user exist in database
        User user = repository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("No user found"));

        // check if the token is valid
        if(jwtService.isValidRefreshToken(token, user)) {
            // generate access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ApiResponse<>(true, "Token refreshed successfully", accessToken);
        }

        return new ApiResponse<>(false, "Invalid refresh token", null);

    }

    public ApiResponse<Void> changePassword(ChangePassword changePassword){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate the current password
        if (!passwordEncoder.matches(changePassword.getPassword(), user.getPassword())) {
            return new ApiResponse<>(false, "Current password is incorrect", null);
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        repository.save(user);

        return new ApiResponse<>(true, "Password changed successfully", null);

    }
}
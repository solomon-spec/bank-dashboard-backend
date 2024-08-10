package com.a2sv.bankdashboard.service;


import com.a2sv.bankdashboard.dto.request.UserRequest;
import com.a2sv.bankdashboard.dto.response.ApiResponse;
import com.a2sv.bankdashboard.dto.response.PublicUserResponse;
import com.a2sv.bankdashboard.dto.response.UserResponse;
import com.a2sv.bankdashboard.model.User;
import com.a2sv.bankdashboard.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    public ApiResponse<UserResponse> update(UserRequest request) {
        User user = repository.findByUsername(request.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));


        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPermanentAddress(request.getPermanentAddress());
        user.setPostalCode(request.getPostalCode());
        user.setPresentAddress(request.getPresentAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setProfilePicture(request.getProfilePicture());
        user = repository.save(user);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDateOfBirth(),
                user.getPermanentAddress(),
                user.getPostalCode(),
                user.getUsername(),
                user.getPresentAddress(),
                user.getCity(),
                user.getCountry(),
                user.getProfilePicture(),
                user.getAccountCash(),
                user.getRole()
        );

        return new ApiResponse<>(true, "User updated successfully", userResponse);
    }

    public ApiResponse<?> getUser(String username) {
        // Get the current authenticated user's username
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = repository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        if (username.equals(currentUsername)) {
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getDateOfBirth(),
                    user.getPermanentAddress(),
                    user.getPostalCode(),
                    user.getUsername(),
                    user.getPresentAddress(),
                    user.getCity(),
                    user.getCountry(),
                    user.getProfilePicture(),
                    user.getAccountCash(),
                    user.getRole()
            );
            return new ApiResponse<>(true, "User found", userResponse);
        } else {
            PublicUserResponse publicUserResponse = new PublicUserResponse(
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getCity(),
                    user.getCountry(),
                    user.getProfilePicture()
            );
            return new ApiResponse<>(true, "User found", publicUserResponse);
        }
    }
}
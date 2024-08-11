package com.a2sv.bankdashboard.dto.response;

import com.a2sv.bankdashboard.model.Preference;
import com.a2sv.bankdashboard.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Date dateOfBirth;
    private String permanentAddress;
    private String postalCode;
    private String username;
    private String presentAddress;
    private String city;
    private String country;
    private String profilePicture;
    private Double accountCash;
    private Role role;
    private Preference preference;
}
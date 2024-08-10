package com.a2sv.bankdashboard.dto.request;

import com.a2sv.bankdashboard.model.Preference;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotNull(message = "Date of Birth is mandatory")
    @Past(message = "Date of Birth should be in the past")
    private Date dateOfBirth;

    @NotBlank(message = "Permanent Address is mandatory")
    private String permanentAddress;

    @NotBlank(message = "Postal Code is mandatory")
    private String postalCode;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

    @NotBlank(message = "Present Address is mandatory")
    private String presentAddress;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Country is mandatory")
    private String country;

    private String profilePicture;

    private Preference preference;

}
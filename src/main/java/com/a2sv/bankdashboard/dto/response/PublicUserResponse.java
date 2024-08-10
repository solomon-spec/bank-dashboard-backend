package com.a2sv.bankdashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserResponse {
    private Integer id;
    private String name;
    private String username;
    private String city;
    private String country;
    private String profilePicture;
}
package com.a2sv.bankdashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tokens")
public class Token {

    @Id
    private String id;

    private String accessToken;
    private String refreshToken;
    private boolean loggedOut;

    @DBRef
    private User user;
}
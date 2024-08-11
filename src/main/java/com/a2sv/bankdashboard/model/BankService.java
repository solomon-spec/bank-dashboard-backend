package com.a2sv.bankdashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "bankServices")
public class BankService {
    @Id
    private String id;
    private String name;
    private String details;
    private int numberOfUsers;
    private String status;
    private String type;
    private String icon;
}
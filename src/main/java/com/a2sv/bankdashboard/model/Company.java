package com.a2sv.bankdashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "companies")
public class Company {
    @Id
    private String id;

    private String companyName;
    private String type;
    private String icon;
}
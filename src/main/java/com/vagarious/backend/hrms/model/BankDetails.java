package com.vagarious.backend.hrms.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BankDetails {
    private String accountNumber;
    private String bankName;
    private String ifsc;
    private String branch;
}


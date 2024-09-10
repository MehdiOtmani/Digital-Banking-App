package com.projects.ebanking_backend.dtos;

import com.projects.ebanking_backend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data
public class CurrentBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private Double overDraft;
}

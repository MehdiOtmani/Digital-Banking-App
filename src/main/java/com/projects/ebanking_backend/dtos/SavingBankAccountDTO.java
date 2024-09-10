package com.projects.ebanking_backend.dtos;

import com.projects.ebanking_backend.entities.AccountOperation;
import com.projects.ebanking_backend.entities.Customer;
import com.projects.ebanking_backend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data

public class SavingBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}

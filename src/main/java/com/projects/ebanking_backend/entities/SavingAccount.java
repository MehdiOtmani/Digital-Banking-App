package com.projects.ebanking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("SA")
@Data @NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
public class SavingAccount extends BankAccount{
    private double interestRate;
}

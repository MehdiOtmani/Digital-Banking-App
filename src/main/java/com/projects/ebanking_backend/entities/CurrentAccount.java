package com.projects.ebanking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("CUR")
@Data @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CurrentAccount extends BankAccount {
    private double overDraft;
}

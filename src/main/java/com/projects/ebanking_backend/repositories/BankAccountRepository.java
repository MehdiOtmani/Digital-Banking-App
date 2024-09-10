package com.projects.ebanking_backend.repositories;

import com.projects.ebanking_backend.entities.BankAccount;
import com.projects.ebanking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,String> {

}

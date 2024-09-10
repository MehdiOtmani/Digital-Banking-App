package com.projects.ebanking_backend;

import com.projects.ebanking_backend.dtos.BankAccountDTO;
import com.projects.ebanking_backend.dtos.CurrentBankAccountDTO;
import com.projects.ebanking_backend.dtos.CustomerDTO;
import com.projects.ebanking_backend.dtos.SavingBankAccountDTO;
import com.projects.ebanking_backend.entities.*;
import com.projects.ebanking_backend.enums.AccountStatus;
import com.projects.ebanking_backend.enums.OperationType;
import com.projects.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.projects.ebanking_backend.exceptions.CustomerNotFoundException;
import com.projects.ebanking_backend.repositories.AccountOperationRepository;
import com.projects.ebanking_backend.repositories.BankAccountRepository;
import com.projects.ebanking_backend.repositories.CustomerRepository;
import com.projects.ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}


	@Bean
	CommandLineRunner start(BankAccountService bankAccountService) {
		return args -> {
				Stream.of("Hamza", "Mehdi", "Yassine").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
				bankAccountService.listCutomers().forEach(cust -> {
				try {
					bankAccountService.saveSavingBankAccount(Math.random() * 100000, 5.5, cust.getId());
					bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 9000.0, cust.getId());
				} catch (CustomerNotFoundException e) {
					throw new RuntimeException(e);
				}
			});
				List<BankAccountDTO> bankAccounts = bankAccountService.listOfAccounts();
				for(BankAccountDTO bankAccount:bankAccounts)
					for (int i = 0; i < 5; i++) {
						String accountId;
						if (bankAccount instanceof CurrentBankAccountDTO){
							accountId=((CurrentBankAccountDTO) bankAccount).getId();
						}else {
							accountId=((SavingBankAccountDTO) bankAccount).getId();
						}
					bankAccountService.credit(accountId, 10000 + Math.random() * 12000, "Credit");
					bankAccountService.debit(accountId, 10000, "Debit");

			}
		};
	}
}



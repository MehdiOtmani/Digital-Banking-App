package com.projects.ebanking_backend.web;

import com.projects.ebanking_backend.dtos.AccountHistoryDTO;
import com.projects.ebanking_backend.dtos.AccountOperationDTO;
import com.projects.ebanking_backend.dtos.BankAccountDTO;
import com.projects.ebanking_backend.entities.BankAccount;
import com.projects.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.projects.ebanking_backend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts() {
        return bankAccountService.listOfAccounts();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId, @RequestParam (name = "page",defaultValue ="0")int page, @RequestParam(name = "size",defaultValue = "5") int size){
        return bankAccountService.getAccountHistory(accountId,page,size);
    }



}

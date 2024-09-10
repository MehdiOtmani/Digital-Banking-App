package com.projects.ebanking_backend.services;

import com.projects.ebanking_backend.dtos.*;
import com.projects.ebanking_backend.exceptions.BalanceNotSufficientException;
import com.projects.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.projects.ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Double OverDraft, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCutomers();
    List<BankAccountDTO> listOfAccounts();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<AccountOperationDTO> accountHistory(String accountId);
    AccountHistoryDTO getAccountHistory(String accountId,int page,int size);
}

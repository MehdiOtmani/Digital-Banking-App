package com.projects.ebanking_backend.services;

import com.projects.ebanking_backend.dtos.*;
import com.projects.ebanking_backend.entities.*;
import com.projects.ebanking_backend.enums.OperationType;
import com.projects.ebanking_backend.exceptions.BalanceNotSufficientException;
import com.projects.ebanking_backend.exceptions.BankAccountNotFoundException;
import com.projects.ebanking_backend.exceptions.CustomerNotFoundException;
import com.projects.ebanking_backend.mappers.BankAccountMapperImpl;
import com.projects.ebanking_backend.repositories.AccountOperationRepository;
import com.projects.ebanking_backend.repositories.BankAccountRepository;
import com.projects.ebanking_backend.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer=dtoMapper.fromCustomerDto(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return  dtoMapper.fromCustomer(savedCustomer) ;
    }


    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException{
        SavingAccount savingAccount;
        Customer customer=customerRepository.findById(customerId).orElseThrow(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedSavingAccount=bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(savedSavingAccount);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Double overDraft, Long customerId) throws CustomerNotFoundException {
       CurrentAccount currentAccount;
       Customer customer=customerRepository.findById(customerId).orElseThrow(null);
       if(customer == null)
           throw new CustomerNotFoundException("Customer Not Found");
        currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(customer);
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedCurrentAccount=bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentAccount(savedCurrentAccount);
    }

    @Override
    public List<CustomerDTO> listCutomers() {
        //Programmation Imperative
        List<Customer> customers=customerRepository.findAll();
        List<CustomerDTO> customersDTO=new ArrayList<>();
        for(Customer customer:customers){
              customersDTO.add(dtoMapper.fromCustomer(customer));
        }
        return  customersDTO;


        /*or Programmation Fonctionelle
        List<Customer> customers=customerRepository.findAll();
         List<CustomerDTO> customersDTO=customers
        List<CustomerDTO> customersDTO=customers.stream().map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        */

    }
    @Override
    public List<BankAccountDTO> listOfAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountsDTO=bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountsDTO;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException{
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bankAccount Not Found"));
        if (bankAccount instanceof SavingAccount)
            return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
        else
            return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount );
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
    BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account Not Found"));
    if(bankAccount.getBalance()<amount)
        throw new BalanceNotSufficientException("Balance Not suffiscient For The Operation");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("Bank Account Not Found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
    debit(accountIdSource,amount,"Transfer To"+accountIdDestination);
    credit(accountIdDestination,amount,"Transfer From "+accountIdSource);
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer Not Found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating Customer");
        Customer customer=dtoMapper.fromCustomerDto(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return  dtoMapper.fromCustomer(savedCustomer) ;
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperation=accountOperationRepository.findByBankAccountId(accountId);
        return accountOperation.stream().map(accountOperation1 -> dtoMapper.fromAccountOperation(accountOperation1)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId,int page,int size){
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(null);
        Page<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.getContent().stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;






    }
}

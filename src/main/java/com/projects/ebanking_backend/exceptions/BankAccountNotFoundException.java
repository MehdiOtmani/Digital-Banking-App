package com.projects.ebanking_backend.exceptions;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException(String message){
        super(message);
    }
}

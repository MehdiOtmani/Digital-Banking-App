package com.projects.ebanking_backend.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message){
            super(message);
    }
}

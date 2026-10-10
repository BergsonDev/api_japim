package com.japim.exceptions;

import org.springframework.graphql.execution.ErrorType;

public abstract class DomainException extends RuntimeException{
    
    protected ErrorType error;

    protected DomainException(String message, ErrorType error){
        super(message);
        this.error = error;
    }

    public ErrorType getErrorType(){
        return this.error;
    }
}
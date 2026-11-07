package com.japim.exceptions;

import org.springframework.graphql.execution.ErrorType;

public class FunctionalityIsCommingException extends DomainException {
    public FunctionalityIsCommingException(String message) {
        super(message != null ? message : "Funcionalidade em desenvolvimento.", ErrorType.BAD_REQUEST);
    }
    
}

package com.japim.exceptions;

public class FunctionalityIsCommingException extends DomainException {
    public FunctionalityIsCommingException(String message) {
        super(message != null ? message : "Funcionalidade em desenvolvimento.", null);
    }
    
}

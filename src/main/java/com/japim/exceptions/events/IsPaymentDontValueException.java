package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class IsPaymentDontValueException extends DomainException {
    public IsPaymentDontValueException() {
        super("O valor do evento deve ser maior que zero se o evento for pago.", ErrorType.BAD_REQUEST);
    }
    
}

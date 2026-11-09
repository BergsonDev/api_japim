package com.japim.exceptions.eventPresence;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class DateDontValidateInEventException extends DomainException{
    public DateDontValidateInEventException(){
        super("Data informada não está no intervalo do evento", ErrorType.BAD_REQUEST);
    }
}
package com.japim.exceptions.certificates;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class DaysDiffOfEventPresenceException extends DomainException{
    public DaysDiffOfEventPresenceException(){
        super("Dias não contabilizados para se ter o certificado", ErrorType.BAD_REQUEST);
    }
}

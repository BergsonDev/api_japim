package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class DontUpdateEventException extends DomainException{
    public DontUpdateEventException(String msg){
        super(msg != null ? msg : "Este evento não pode ser atualizado", ErrorType.BAD_REQUEST);
    }
}

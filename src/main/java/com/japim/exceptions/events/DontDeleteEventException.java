package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class DontDeleteEventException extends DomainException{
    public DontDeleteEventException(String msg){
        super(msg != null ? msg : "Este evento não pode ser deletado", ErrorType.BAD_REQUEST);
    }
}

package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class EventDontModifyStatusException extends DomainException{
    public EventDontModifyStatusException(String msg){
        super(msg != null ? msg : "Evento não pode ser modificado por está encerrado ou cancelado",ErrorType.BAD_REQUEST);
    }    
}

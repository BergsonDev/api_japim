package com.japim.exceptions.eventPresence;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;


public class PresenceConfirmatedInEventException extends DomainException{
    public PresenceConfirmatedInEventException(){
        super("Presença já registrada para este participante", ErrorType.BAD_REQUEST);
    }
}
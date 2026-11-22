package com.japim.exceptions.eventPresence;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;


public class DontPresenceConfirmatedInEventException extends DomainException{
    public DontPresenceConfirmatedInEventException(){
        super("Não há presença já registrada para este participante", ErrorType.BAD_REQUEST);
    }
}
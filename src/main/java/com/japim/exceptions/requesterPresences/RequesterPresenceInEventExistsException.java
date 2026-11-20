package com.japim.exceptions.requesterPresences;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class RequesterPresenceInEventExistsException extends DomainException{
    
    public RequesterPresenceInEventExistsException(){
        super("Solicitação para presença no evento já foi confirmado, no aguardo da validação dos voluntários, coordeanação ou palestrante",ErrorType.BAD_REQUEST);
    }
}

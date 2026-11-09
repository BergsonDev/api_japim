package com.japim.exceptions.eventPresence;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class TimeDontValueForPresenceException extends DomainException{
    public TimeDontValueForPresenceException(String message){
        super(message != null ? message : "Data ou hora inválidos baseado no evento para marcar a presença", ErrorType.BAD_REQUEST);
    }
}

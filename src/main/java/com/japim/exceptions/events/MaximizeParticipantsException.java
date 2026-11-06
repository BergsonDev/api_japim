package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class MaximizeParticipantsException extends DomainException {
    public MaximizeParticipantsException() {
        super("O número máximo de participantes do evento foi atingido.", ErrorType.BAD_REQUEST);
    }

    
}
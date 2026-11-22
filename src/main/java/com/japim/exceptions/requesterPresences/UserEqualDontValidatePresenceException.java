package com.japim.exceptions.requesterPresences;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class UserEqualDontValidatePresenceException extends DomainException{
    public UserEqualDontValidatePresenceException(){
        super("O mesmo usuário não se pode auto avaliar", ErrorType.BAD_REQUEST);
    }
}

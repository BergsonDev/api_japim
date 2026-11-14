package com.japim.exceptions.certificates;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class UserDontExistsInEventException extends DomainException{
    public UserDontExistsInEventException(){
        super("Usuário não se encontra neste evento !",ErrorType.BAD_REQUEST);
    }
    
}

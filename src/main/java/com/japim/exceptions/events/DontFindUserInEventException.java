package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class DontFindUserInEventException extends DomainException{
    public DontFindUserInEventException(){
        super("Usuário não se encontra no evento", ErrorType.BAD_REQUEST);
    }
}
package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class FindUserInEventException extends DomainException{
    public FindUserInEventException(){
        super("Usuário já se encontra no evento", ErrorType.BAD_REQUEST);
    }
}
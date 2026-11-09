package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class DontPermissionApprovedEventException extends DomainException{
    public DontPermissionApprovedEventException(){
        super("Usuário não tem a permissão para aprovar o evento", ErrorType.BAD_REQUEST);
    }
}
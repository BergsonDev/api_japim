package com.japim.exceptions.requesterPresences;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class CodeRequesterPresenceDontExistsException extends DomainException{
    public CodeRequesterPresenceDontExistsException(){
        super("Código não existente para validação", ErrorType.BAD_REQUEST);
    }
}

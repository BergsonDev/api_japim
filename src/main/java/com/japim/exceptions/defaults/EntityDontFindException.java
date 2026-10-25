package com.japim.exceptions.defaults;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;



public class EntityDontFindException extends DomainException{
    public EntityDontFindException(Long id, String entity){
        String message = id == null ? String.format("'%s' not found", entity) : String.format("'%s' with id '%d' was not found", entity, id);
        super(message, ErrorType.NOT_FOUND);
    }
}
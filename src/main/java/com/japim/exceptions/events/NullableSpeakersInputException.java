package com.japim.exceptions.events;

import org.springframework.graphql.execution.ErrorType;

import com.japim.exceptions.DomainException;

public class NullableSpeakersInputException extends DomainException {
    public NullableSpeakersInputException() {
        super("Speakers input cannot be null or empty",ErrorType.BAD_REQUEST);
    }
}

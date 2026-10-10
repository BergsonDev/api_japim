package com.japim.graphql.resolvers;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import com.japim.exceptions.DomainException;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter{

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env){
        if(ex instanceof DomainException){
            return GraphqlErrorBuilder.newError()
                    .errorType(((DomainException) ex).getErrorType())
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }

        return null;

    }
}
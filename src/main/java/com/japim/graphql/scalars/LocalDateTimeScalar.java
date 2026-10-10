package com.japim.graphql.scalars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;


public class LocalDateTimeScalar {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static final GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("LocalDateTime")
            .description("Scalar customizado para java.time.LocalDateTime no formato ISO-8601")
            .coercing(new Coercing<LocalDateTime, String>() {
                @Override
                public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    if (dataFetcherResult instanceof LocalDateTime localDateTime) {
                        return localDateTime.format(FORMATTER);
                    }
                    throw new CoercingSerializeException("Não foi possível serializar: " + dataFetcherResult);
                }

                @Override
                public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
                    if (input instanceof String stringValue) {
                        try {
                            return LocalDateTime.parse(stringValue, FORMATTER);
                        } catch (Exception e) {
                            throw new CoercingParseValueException("Erro ao converter string para LocalDateTime: " + input, e);
                        }
                    }
                    throw new CoercingParseValueException("Input inválido. Deve ser uma String.");
                }

                @Override
                public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                    if (input instanceof StringValue stringValue) {
                        try {
                            return LocalDateTime.parse(stringValue.getValue(), FORMATTER);
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Erro ao parsear literal GraphQL", e);
                        }
                    }
                    throw new CoercingParseLiteralException("Literal inválido. Esperada uma String.");
                }
            })
            .build();
}

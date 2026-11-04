package com.japim.graphql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.japim.graphql.scalars.LocalDateTimeScalar;

import graphql.scalars.ExtendedScalars;
@Configuration
public class GraphQLConfig {


    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(LocalDateTimeScalar.INSTANCE)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.GraphQLLong)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.Time)
                .scalar(ExtendedScalars.UUID)
                .scalar(ExtendedScalars.Json)
                .scalar(ExtendedScalars.LocalTime);
    }
}

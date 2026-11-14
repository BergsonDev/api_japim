package com.japim.exceptions.certificates;

import com.japim.exceptions.DomainException;
import org.springframework.graphql.execution.ErrorType;


public class DontGenerateCertificateException extends DomainException {
    
    public DontGenerateCertificateException(){
        super("No momento não é possível gerar o certificado",ErrorType.BAD_REQUEST);
    }
}

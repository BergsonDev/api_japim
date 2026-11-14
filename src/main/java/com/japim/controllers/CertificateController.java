package com.japim.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.japim.dtos.certificates.CertificateDTO;
import com.japim.services.CertificateService;

@Controller 
public class CertificateController {
    
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService){
        this.certificateService = certificateService;
    }

    @QueryMapping
    public List<CertificateDTO> certificates(){
        return certificateService.certificates();
    }

    @QueryMapping 
    public CertificateDTO certificate(@Argument Long id){
        return certificateService.certificate(id);
    }

    @QueryMapping
    public List<CertificateDTO> myCertificates(Authentication auth){
        return certificateService.myCertificates(auth);
    }

    @MutationMapping 
    public CertificateDTO getMyCertificate(@Argument Long event_id, Authentication auth){
        return certificateService.getMyCertificate(auth,event_id);
    }
}

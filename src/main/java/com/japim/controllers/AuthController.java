package com.japim.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.japim.dtos.users.AuthResponseDTO;
import com.japim.dtos.users.LoginInputDTO;
import com.japim.services.JwtService;

@Controller
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @MutationMapping
    public AuthResponseDTO login(@Argument LoginInputDTO input) {
        // Autentica usando o AuthenticationManager do Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.username(), input.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }

}

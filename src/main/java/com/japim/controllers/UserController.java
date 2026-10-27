package com.japim.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.japim.dtos.users.UserDTO;
import com.japim.dtos.users.UserInputDTO;
import com.japim.services.UserService;
@Controller 
public class UserController {

    private final UserService userService;
    
    UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('users-read')")
    public List<UserDTO> users(){
        return this.userService.findAll();
    } 

    @QueryMapping
    @PreAuthorize("hasAuthority('users-read')")
    public UserDTO user(@Argument long id){
        return this.userService.findByID(id);
    }
    
    @MutationMapping
    @PreAuthorize("hasAuthority('users-create')")
    public UserDTO createUser(@Argument("input") UserInputDTO userInput){
        return this.userService.create(userInput);

    }
    @MutationMapping 
    @PreAuthorize("hasAuthority('users-update')")
    public UserDTO updateUser(@Argument("input") UserInputDTO userInput){
        return this.userService.update(userInput);

    }

    @MutationMapping 
    @PreAuthorize("hasAuthority('users-delete')")
    public UserDTO delete(@Argument long id){
        return this.userService.delete(id);
    }

    @MutationMapping 
    @PreAuthorize("hasAuthority('users-update')")
    public UserDTO desativeAccount(@Argument long id){
        return this.userService.desative(id);
    }
}

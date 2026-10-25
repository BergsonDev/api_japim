package com.japim.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.japim.dtos.users.UserDTO;
import com.japim.dtos.users.UserInputDTO;
import com.japim.services.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
@Controller 
public class UserController {

    private final UserService userService;
    
    UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public List<UserDTO> users(){
        return this.userService.findAll();
    } 

    @QueryMapping 
    public UserDTO user(@Argument long id){
        return this.userService.findByID(id);
    }
    
    @MutationMapping 
    public UserDTO createUser(@Argument("input") UserInputDTO userInput){
        return this.userService.create(userInput);

    }
    @MutationMapping 
    public UserDTO updateUser(@Argument("input") UserInputDTO userInput){
        return this.userService.update(userInput);

    }

    @MutationMapping 
    public UserDTO delete(@Argument long id){
        return this.userService.delete(id);
    }

    @MutationMapping 
    public UserDTO desativeAccount(@Argument long id){
        return this.userService.desative(id);
    }
}

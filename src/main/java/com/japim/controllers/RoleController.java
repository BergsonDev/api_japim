package com.japim.controllers;

import com.japim.dtos.RoleDTO;
import com.japim.services.RoleService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class RoleController {
    private final RoleService roleService;

    RoleController(RoleService role){
        roleService = role;
    }

    @QueryMapping
    List<RoleDTO> roles(){
        return this.roleService.all();
    }

}

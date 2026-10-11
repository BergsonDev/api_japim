package com.japim.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.japim.dtos.permissions.PermissionDTO;
import com.japim.dtos.permissions.PermissionInputCreateDTO;
import com.japim.dtos.permissions.PermissionInputUpdateDTO;
import com.japim.services.PermissionService;

@Controller
public class PermissionController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    private final PermissionService permissionService;

    PermissionController(PermissionService permission){
        permissionService = permission;
    }

    @QueryMapping
    List<PermissionDTO> permissions(){
        return this.permissionService.all();
    }

    @QueryMapping
    PermissionDTO permission(@Argument Long id){
        return this.permissionService.findByID(id);
    }

    @MutationMapping
    PermissionDTO createPermission(@Argument("input") PermissionInputCreateDTO permission){ return this.permissionService.create(permission);}

    @MutationMapping
    PermissionDTO updatePermission(@Argument("input") PermissionInputUpdateDTO permission){ return  this.permissionService.update(permission); }

    @MutationMapping
    PermissionDTO deletePermission(@Argument Long id){ return this.permissionService.delete(id); }
}

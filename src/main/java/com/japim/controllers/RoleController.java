package com.japim.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.japim.dtos.roles.RoleDTO;
import com.japim.dtos.roles.RoleInputCreateDTO;
import com.japim.dtos.roles.RoleInputPermissionDTO;
import com.japim.dtos.roles.RoleInputUpdateDTO;
import com.japim.services.RoleService;

@Controller
public class RoleController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    private final RoleService roleService;

    RoleController(RoleService role){
        roleService = role;
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('roles-read')")
    List<RoleDTO> roles(){
        return this.roleService.all();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('roles-read')")
    RoleDTO role(@Argument Long id){
        return this.roleService.findByID(id);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('roles-create')")
    RoleDTO createRole(@Argument("input") RoleInputCreateDTO role){ return this.roleService.create(role);}

    @MutationMapping
    @PreAuthorize("hasAuthority('roles-update')")
    RoleDTO updateRole(@Argument("input") RoleInputUpdateDTO role){ return  this.roleService.update(role); }

    @MutationMapping
    @PreAuthorize("hasAuthority('roles-create')")
    RoleDTO deleteRole(@Argument Long id){ return this.roleService.delete(id); }

    @MutationMapping
    @PreAuthorize("hasAuthority('permissions-update')")
    RoleDTO addPermission(@Argument("input") RoleInputPermissionDTO rolePermission){ return this.roleService.addPermissions(rolePermission); };
    
    @MutationMapping
    @PreAuthorize("hasAuthority('permissions-update')")
    RoleDTO removePermission(@Argument("input") RoleInputPermissionDTO rolePermission){ return this.roleService.removePermissions(rolePermission); };
    
}

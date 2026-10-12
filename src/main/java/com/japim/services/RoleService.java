package com.japim.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.roles.RoleDTO;
import com.japim.dtos.roles.RoleInputCreateDTO;
import com.japim.dtos.roles.RoleInputPermissionDTO;
import com.japim.dtos.roles.RoleInputUpdateDTO;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Permission;
import com.japim.models.Role;
import com.japim.repositories.PermissionRepository;
import com.japim.repositories.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;

    private RoleDTO parseDTO(Role role){
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getInitialism(),
                role.getDescription(),
                role.getPermissions(),
                role.getCreated_at(),
                role.getUpdated_at(),
                role.getDeleted_at()
        );
    }

    RoleService(RoleRepository roleRepo, PermissionRepository permissionRepo){
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> all(){
        return this.roleRepo.findAll().stream().map(this::parseDTO).toList();
    }

    @Transactional(readOnly=true)
    public RoleDTO findByID(Long id){
        Role role = this.roleRepo.findById(id).orElseThrow(()->new EntityDontFindException(id,"roles"));
        return this.parseDTO(role);
    }

    public RoleDTO create(RoleInputCreateDTO roleInput){
        Role role = new Role();
        role.setName(roleInput.name());
        role.setInitialism(roleInput.initialism());
        role.setDescription(roleInput.description());

        Role role_save = this.roleRepo.save(role);

        return this.parseDTO(role_save);
    }


    public RoleDTO update(RoleInputUpdateDTO roleInput){
        Role role = roleRepo.findById(roleInput.id()).orElseThrow(()->new EntityDontFindException(roleInput.id(), "roles"));

        role.setName(roleInput.name());
        role.setInitialism(roleInput.initialism());
        role.setDescription(roleInput.description());


        Role updated = roleRepo.save(role);
        return this.parseDTO(updated);
    }

    public RoleDTO delete(Long id){
        Role role = roleRepo.findById(id).orElseThrow(()->new EntityDontFindException(id, "roles"));

        this.roleRepo.delete(role);

        return this.parseDTO(role);
    }

    @Transactional
    public RoleDTO addPermissions(RoleInputPermissionDTO rolesInputPermissions){
        Role role = this.roleRepo.findById(rolesInputPermissions.id()).orElseThrow(()->new EntityDontFindException(rolesInputPermissions.id(), "roles"));
        Set<Permission> permissions =  new HashSet<Permission>();
        rolesInputPermissions.permissions_id().forEach(id->{
            Permission permission = this.permissionRepo.findById(id).orElseThrow(()->new EntityDontFindException(id,"permissions"));
            permissions.add(permission);
        });

        permissions.forEach(p->{
            role.addPermission(p);
        });

        this.roleRepo.save(role);
        return this.parseDTO(role);
    }

    @Transactional
    public RoleDTO removePermissions(RoleInputPermissionDTO rolesInputPermissions){
        Role role = this.roleRepo.findById(rolesInputPermissions.id()).orElseThrow(()->new EntityDontFindException(rolesInputPermissions.id(), "roles"));
        Set<Permission> permissions =  new HashSet<Permission>();
        rolesInputPermissions.permissions_id().forEach(id->{
            Permission permission = this.permissionRepo.findById(id).orElseThrow(()->new EntityDontFindException(id,"permissions"));
            permissions.add(permission);
        });

        permissions.forEach(p->{
            role.removePermission(p);
        });

        this.roleRepo.save(role);
        return this.parseDTO(role);
    }

}

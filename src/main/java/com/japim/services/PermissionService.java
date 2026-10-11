package com.japim.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.permissions.PermissionDTO;
import com.japim.dtos.permissions.PermissionInputCreateDTO;
import com.japim.dtos.permissions.PermissionInputUpdateDTO;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Permission;
import com.japim.repositories.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepo;

    private PermissionDTO parseDTO(Permission permission){
        return new PermissionDTO(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getCreated_at(),
                permission.getUpdated_at(),
                permission.getDeleted_at()
        );
    }

    PermissionService(PermissionRepository permissionRepo){
        this.permissionRepo = permissionRepo;
    }

    @Transactional(readOnly = true)
    public List<PermissionDTO> all(){
        return this.permissionRepo.findAll().stream().map(this::parseDTO).toList();
    }

    @Transactional(readOnly=true)
    public PermissionDTO findByID(Long id){
        Permission permission = this.permissionRepo.findById(id).orElseThrow(()->new EntityDontFindException(id,"permission"));
        return this.parseDTO(permission);
    }

    public PermissionDTO create(PermissionInputCreateDTO permissionInput){
        Permission permission = new Permission();
        permission.setName(permissionInput.name());
        permission.setDescription(permissionInput.description());

        Permission permission_save = this.permissionRepo.save(permission);

        return new PermissionDTO(permission_save.getId(), permission_save.getName(), permission_save.getDescription(), permission_save.getCreated_at(), permission_save.getUpdated_at(), permission_save.getDeleted_at());
    }


    public PermissionDTO update(PermissionInputUpdateDTO permissionInput){
        Permission permission = permissionRepo.findById(permissionInput.id()).orElseThrow(()->new EntityDontFindException(permissionInput.id(), "permission"));

        permission.setName(permissionInput.name());
        permission.setDescription(permissionInput.description());


        Permission updated = permissionRepo.save(permission);
        return this.parseDTO(updated);
    }

    public PermissionDTO delete(Long id){
        Permission permission = permissionRepo.findById(id).orElseThrow(()->new EntityDontFindException(id, "permission"));

        this.permissionRepo.delete(permission);

        return this.parseDTO(permission);
    }

}

package com.japim.services;

import com.japim.dtos.RoleDTO;
import com.japim.models.Role;
import com.japim.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepo ;

    private RoleDTO parseDTO(Role role){
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getInitialism(),
                role.getDescription(),
                null,
                null,
                null
        );
    }
    RoleService(RoleRepository roleRepo){
        this.roleRepo = roleRepo;
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> all(){
        return this.roleRepo.findAll().stream().map(this::parseDTO).toList();
    }


}

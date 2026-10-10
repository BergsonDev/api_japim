package com.japim.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.japim.dtos.roles.RoleDTO;
import com.japim.dtos.roles.RoleInputCreateDTO;
import com.japim.dtos.roles.RoleInputUpdateDTO;
import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Role;
import com.japim.repositories.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepo ;

    private RoleDTO parseDTO(Role role){
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getInitialism(),
                role.getDescription(),
                role.getCreated_at(),
                role.getUpdated_at(),
                role.getDeleted_at()
        );
    }

    RoleService(RoleRepository roleRepo){
        this.roleRepo = roleRepo;
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

        return new RoleDTO(role_save.getId(), role_save.getName(), role_save.getInitialism(), role_save.getDescription(), role_save.getCreated_at(), role_save.getUpdated_at(), role_save.getDeleted_at());
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

}

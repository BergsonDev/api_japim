package com.japim.seeders.roles;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.japim.models.Role;
import com.japim.repositories.RoleRepository;
import com.japim.seeders.InterfaceSeeder;

@Component 
@Order(2)
public class RoleBasicSeeder implements InterfaceSeeder {

    private RoleRepository roleRepo;

    public RoleBasicSeeder(RoleRepository roleRepo){
        this.roleRepo = roleRepo;
    }

    @Override 
    public String nameSeeder(){
        return "Perfis de Usuários Básicos";
    }


    @Override 
    public void run(){
        Role roleAdmin = new Role();
        Role roleUsers = new Role();
        Role roleStudents = new Role();
        Role roleCoordinator = new Role();
        Role roleSpeaker = new Role();
        Role roleDirector = new Role();

        roleAdmin.setName("Administrador");
        roleAdmin.setInitialism("ADMIN");
        roleAdmin.setDescription("Perfil para gerenciar a nível de comportamento e liberação de acesso no sistema");
        
        roleUsers.setName("Usuários");
        roleUsers.setInitialism("USER");
        roleUsers.setDescription("Perfil para usuários na qual não fazem parte do instituto");

        roleStudents.setName("Estudantes");
        roleStudents.setInitialism("STU");
        roleStudents.setDescription("Perfil para estudantes do instituto.");

        roleCoordinator.setName("Coordenador");
        roleCoordinator.setInitialism("COORD");
        roleCoordinator.setDescription("Perfil para coordenadores");

        roleSpeaker.setName("Palestrante");
        roleSpeaker.setInitialism("SPE");
        roleSpeaker.setDescription("Perfil para palestrantes");
        
        roleDirector.setName("Diretor");
        roleDirector.setInitialism("DIR");
        roleDirector.setDescription("Perfil para diretores");


        roleRepo.saveAll(List.of(roleAdmin, roleUsers, roleStudents, roleCoordinator, roleSpeaker, roleDirector));
    }
}

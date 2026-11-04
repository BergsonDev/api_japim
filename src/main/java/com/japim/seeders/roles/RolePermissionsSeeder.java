package com.japim.seeders.roles;

import java.util.List;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // Ajuste para o nome correto da sua interface, se necessário

import com.japim.models.Permission;
import com.japim.models.Role;
import com.japim.repositories.PermissionRepository;
import com.japim.repositories.RoleRepository;
import com.japim.seeders.InterfaceSeeder;

@Component
@Order(3) // Defina a ordem de execução (por exemplo, após o RoleBasicSeeder e a criação das permissões)
public class RolePermissionsSeeder implements InterfaceSeeder {

    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;

    public RolePermissionsSeeder(RoleRepository roleRepo, PermissionRepository permissionRepo) {
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    public String nameSeeder() {
        return "Vincular Permissões ao Perfil Administrador";
    }

    @Override
    @Transactional 
    public void run() {
        Optional<Role> roleAdminOpt = roleRepo.findByInitialism("ADMIN");

        if (roleAdminOpt.isEmpty()) {
            System.out.println(">> Role ADMIN não encontrada. Certifique-se de que o RoleBasicSeeder rodou antes.");
            return;
        }

        Role roleAdmin = roleAdminOpt.get();

        List<Permission> allPermissions = permissionRepo.findAll();

        if (allPermissions.isEmpty()) {
            System.out.println(">> Nenhuma permissão encontrada para vincular.");
            return;
        }

        int addedCount = 0;
        for (Permission permission : allPermissions) {
            if (!roleAdmin.getPermissions().contains(permission)) {
                roleAdmin.addPermission(permission);
                addedCount++;
            }
        }

        roleRepo.save(roleAdmin);
        
        System.out.println(">> " + addedCount + " permissões vinculadas com sucesso à role ADMIN!");
    }
}
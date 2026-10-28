package com.japim.seeders.roles;

import com.japim.models.Permission;
import com.japim.models.Role;
import com.japim.repositories.PermissionRepository;
import com.japim.repositories.RoleRepository;
import com.japim.seeders.InterfaceSeeder; // Ajuste para o nome correto da sua interface, se necessário
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional // Mantém a sessão ativa para lidar com o relacionamento e o salvamento em cascata
    public void run() {
        // 1. Busca a role ADMIN cadastrada
        Optional<Role> roleAdminOpt = roleRepo.findByInitialism("ADMIN");

        if (roleAdminOpt.isEmpty()) {
            System.out.println(">> Role ADMIN não encontrada. Certifique-se de que o RoleBasicSeeder rodou antes.");
            return;
        }

        Role roleAdmin = roleAdminOpt.get();

        // 2. Busca todas as permissões existentes no banco
        List<Permission> allPermissions = permissionRepo.findAll();

        if (allPermissions.isEmpty()) {
            System.out.println(">> Nenhuma permissão encontrada para vincular.");
            return;
        }

        // 3. Adiciona cada permissão utilizando o método addPermission da model
        int addedCount = 0;
        for (Permission permission : allPermissions) {
            // Evita duplicatas caso o seeder seja executado mais de uma vez
            if (!roleAdmin.getPermissions().contains(permission)) {
                roleAdmin.addPermission(permission);
                addedCount++;
            }
        }

        // 4. Salva a role atualizada com as permissões vinculadas
        roleRepo.save(roleAdmin);
        
        System.out.println(">> " + addedCount + " permissões vinculadas com sucesso à role ADMIN!");
    }
}
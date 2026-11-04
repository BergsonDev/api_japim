package com.japim.seeders.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.japim.models.Permission;
import com.japim.models.Role;
import com.japim.repositories.PermissionRepository;
import com.japim.repositories.RoleRepository;
import com.japim.seeders.InterfaceSeeder;
@Order(5)
@Component
public class PermissionEventSeeder implements InterfaceSeeder {

   private PermissionRepository permissionRepo;
    private RoleRepository roleRepo;

    public PermissionEventSeeder(PermissionRepository permissionRepo, RoleRepository roleRepo) {
        this.permissionRepo = permissionRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public String nameSeeder() {
        return "Permissões de Eventos";

    }

    @Override 
    public void run() {

        Role role = roleRepo.findByInitialism("ADMIN").orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada"));
        
        Map<String, String> permissoesEvents = Map.of(
            "events-view", "Permite visualizar a lista de eventos",
            "events-read", "Permite visualizar os detalhes de um evento específico",
            "events-create", "Permite criar e cadastrar novos eventos",
            "events-update", "Permite editar e atualizar dados de eventos existentes",
            "events-delete", "Permite excluir eventos do sistema",
            "events-confirm", "Permite confirmar eventos do sistema"
        );

        List<Permission> permissions = new ArrayList<>();
        for (Map.Entry<String, String> entry : permissoesEvents.entrySet()) {
                Permission permission = new Permission();
                permission.setName(entry.getKey());
                permission.setDescription(entry.getValue());
                permissions.add(permission);
                role.addPermission(permission);
        }
        permissionRepo.saveAll(permissions);
        roleRepo.save(role);
    }
}

package com.japim.seeders.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.japim.models.Permission;
import com.japim.repositories.PermissionRepository;
import com.japim.seeders.InterfaceSeeder;

@Component
@Order(1)
public class PermissionBasicSeeder implements InterfaceSeeder {
    private PermissionRepository permissionRepo;

    public PermissionBasicSeeder(PermissionRepository permissionRepo) {
        this.permissionRepo = permissionRepo;
    }

    @Override
    public String nameSeeder() {
        return "Permissões de Usuários";
    }

    @Override
    public void run() {
        Map<String, String> permissoesUsers = Map.of(
                "users-view", "Permite visualizar a lista de usuários",
                "users-read", "Permite visualizar os detalhes de um usuário específico",
                "users-create", "Permite criar e cadastrar novos usuários",
                "users-update", "Permite editar e atualizar dados de usuários existentes",
                "users-delete", "Permite excluir usuários do sistema");

        Map<String, String> permissoesRoles = Map.of(
                "roles-view", "Permite visualizar a lista de roles (perfis de acesso)",
                "roles-read", "Permite visualizar os detalhes de uma role específica",
                "roles-create", "Permite criar e cadastrar novas roles",
                "roles-update", "Permite editar e atualizar dados de roles existentes",
                "roles-delete", "Permite excluir roles do sistema");

        Map<String, String> permissoesPermissions = Map.of(
                "permissions-view", "Permite visualizar a lista de permissões",
                "permissions-read", "Permite visualizar os detalhes de uma permissão específica",
                "permissions-create", "Permite criar e cadastrar novas permissões",
                "permissions-update", "Permite editar e atualizar dados de permissões existentes",
                "permissions-delete", "Permite excluir permissões do sistema");

        List<Permission> permissions = new ArrayList<>();

        // Agrupa todos os mapas em uma lista para iterar de forma unificada
        List<Map<String, String>> permissionsInfos = List.of(permissoesUsers, permissoesRoles, permissoesPermissions);

        for (Map<String, String> mapa : permissionsInfos) {
            for (Map.Entry<String, String> entry : mapa.entrySet()) {
                Permission permission = new Permission();
                permission.setName(entry.getKey());
                permission.setDescription(entry.getValue());
                permissions.add(permission);
            }
        }

        permissionRepo.saveAll(permissions);

    }
}

package  com.japim.seeders.roles;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.Permission;
import com.japim.models.Role;
import com.japim.repositories.PermissionRepository;
import com.japim.repositories.RoleRepository;
import com.japim.seeders.InterfaceSeeder;

@Component
@Order(7)
public class RolePermissionCoordSeeder implements InterfaceSeeder{

    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;
    
    public RolePermissionCoordSeeder(RoleRepository roleRepo, PermissionRepository permissionRepo){
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    
    @Override
    public String nameSeeder() {
        return "Permissões de Eventos para o cargo coordenador";
    }

    @Override
    public void run() {
        Role roleCOORD = roleRepo.findByInitialism("COORD").orElseThrow(()-> new EntityDontFindException(null, "roles"));

        List<String> namesPermissions = List.of(
            "events-view",
            "events-read",
            "events-create",
            "events-delete",
            "events-update"
        );

        List<Permission> allPermissions = permissionRepo.findByNameIn(namesPermissions);


        roleCOORD.addPermissions(allPermissions);
    }

    


}

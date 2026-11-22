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
@Order(8)
public class RolePermissionStudentSeeder implements InterfaceSeeder{

    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;
    
    public RolePermissionStudentSeeder(RoleRepository roleRepo, PermissionRepository permissionRepo){
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    
    @Override
    public String nameSeeder() {
        return "Permissões de Eventos para o cargo estudante";
    }

    @Override
    public void run() {
        Role roleStu = roleRepo.findByInitialism("STU").orElseThrow(()-> new EntityDontFindException(null, "roles"));

        List<String> namesPermissions = List.of(
            "events-view",
            "events-view",
            "events-read",
            "events-create",
            "events-update",
            "events-delete",
            "events-confirm",
            "events-subscribe-participant",
            "events-unsubscribe-participant",
            "events-subscribe-volunteer",
            "events-unsubscribe-volunteer",
            "events-confirm-presence",
            "events-confirm-presence-volunteers"
        );

        List<Permission> allPermissions = permissionRepo.findByNameIn(namesPermissions);


        roleStu.addPermissions(allPermissions);
    }

    


}

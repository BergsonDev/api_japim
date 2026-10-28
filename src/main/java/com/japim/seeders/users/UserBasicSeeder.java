package com.japim.seeders.users;

import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.japim.exceptions.defaults.EntityDontFindException;
import com.japim.models.User;
import com.japim.repositories.RoleRepository;
import com.japim.repositories.UserRepository;
import com.japim.seeders.InterfaceSeeder;

@Component 
@Order(4)
public class UserBasicSeeder implements InterfaceSeeder{
    
    private UserRepository userRepo;
    private PasswordEncoder encoder;
    private RoleRepository roleRepo;
    public UserBasicSeeder(UserRepository userRepo,PasswordEncoder encoder, RoleRepository roleRepo){
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
    }
    
    @Override 
    public String nameSeeder(){
        return "Usuários Básicos";
    }


    @Override 
    public void run(){
        User userAdmin = new User();

        userAdmin.setName("admin");
        userAdmin.setEmail("admin@japim.com");
        userAdmin.setRa("0");
        // userAdmin.setCpf("0"); //deve verificar o banco de dados
        userAdmin.setIs_active(true);
        userAdmin.setPassword(encoder.encode("japimSenhaAdmin@123Bergson!"));
        userAdmin.setRole(roleRepo.findByInitialism("ADMIN").orElseThrow(()->new EntityDontFindException(null, "roles")));
        userRepo.save(userAdmin);

    }
}

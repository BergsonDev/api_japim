package com.japim.seeders.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
@Profile("!production")
public class UserBasicSeeder implements InterfaceSeeder{
    
    private UserRepository userRepo;
    private PasswordEncoder encoder;
    private RoleRepository roleRepo;
    private String adminPassword;
    private String testPassword;

    public UserBasicSeeder(
            UserRepository userRepo,
            PasswordEncoder encoder,
            RoleRepository roleRepo,
            @Value("${SEED_ADMIN_PASSWORD:japimSenhaAdmin@123Bergson!}") String adminPassword,
            @Value("${SEED_TEST_PASSWORD:teste}") String testPassword){
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
        this.adminPassword = adminPassword;
        this.testPassword = testPassword;
    }
    
    @Override 
    public String nameSeeder(){
        return "Usuários Básicos";
    }


    @Override 
    public void run(){
        System.out.println(">> [DEV] Criando usuários de desenvolvimento. Não use este seeder em produção.");

        User userAdmin = new User();

        userAdmin.setName("admin");
        userAdmin.setEmail("admin@japim.com");
        userAdmin.setCpf("0");
        userAdmin.setIs_active(true);
        userAdmin.setPassword(encoder.encode(adminPassword));
        userAdmin.setRole(roleRepo.findByInitialism("ADMIN").orElseThrow(()->new EntityDontFindException(null, "roles")));
        userRepo.save(userAdmin);

        User userTeste = new User();

        userTeste.setName("teste");
        userTeste.setEmail("teste@japim.com");
        userTeste.setCpf("1");
        userTeste.setIs_active(true);
        userTeste.setPassword(encoder.encode(testPassword));
        userTeste.setRole(roleRepo.findByInitialism("COORD").orElseThrow(()->new EntityDontFindException(null, "roles")));
        userRepo.save(userTeste);

    }
}

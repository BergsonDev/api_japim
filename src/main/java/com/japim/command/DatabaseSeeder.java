package com.japim.command;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import com.japim.seeders.InterfaceSeeder;
@Component 
public class DatabaseSeeder implements ApplicationRunner {
    private List<InterfaceSeeder> seeders;
    private final Environment environment;

    public DatabaseSeeder(List<InterfaceSeeder> seeders, Environment environment) {
        this.seeders = seeders;
        this.environment = environment;
    }

    @Override 
    public void run(ApplicationArguments args) throws Exception{
        if(args.containsOption("seeders")){
            if (environment.acceptsProfiles(Profiles.of("production"))) {
                System.out.println(">> [PROD] Executando seeders de dados de referência. O UserBasicSeeder não é carregado e nenhum usuário de teste será criado.");
            }

            System.out.println("=============Iniciando as Seeders=============");
            for(InterfaceSeeder seeder : seeders){
                System.out.println("--------Inicializando Seeder " + seeder.nameSeeder() + "--------");
                seeder.run();
                System.out.println("--------Adicionado com sucesso sobre a  Seeder " + seeder.nameSeeder() + "--------");
            }
            System.out.println("=============Adicionado com sucesso=============");

            System.exit(0);
        }
    }
    
    
}

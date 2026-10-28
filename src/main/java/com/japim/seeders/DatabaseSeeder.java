package com.japim.seeders;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component 
public class DatabaseSeeder implements ApplicationRunner {
    private List<InterfaceSeeder> seeders;
    public DatabaseSeeder(List<InterfaceSeeder> seeders) {
        this.seeders = seeders;
    }

    @Override 
    public void run(ApplicationArguments args) throws Exception{
        if(args.containsOption("seeders")){
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

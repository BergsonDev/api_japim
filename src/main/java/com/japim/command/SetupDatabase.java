package com.japim.command;

import java.util.List;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import com.japim.seeders.InterfaceSeeder;

@Component 
public class SetupDatabase implements ApplicationRunner {

    private final List<InterfaceSeeder> seeders;
    private final DataSource dataSource;
    private final Flyway flyway;
    private final Environment environment;
    
    public SetupDatabase(List<InterfaceSeeder> seeders, DataSource dataSource, Flyway flyway, Environment environment) {
        this.seeders = seeders;
        this.dataSource = dataSource;
        this.flyway = flyway;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("setupDatabase")) {
            if (environment.acceptsProfiles(Profiles.of("production"))) {
                System.err.println("ERRO: setupDatabase é destrutivo e não pode ser executado com o profile 'production'.");
                System.exit(1);
            }

            System.out.println("==============================Configurando as informações para rodar o sistema com o banco de dados==============================");

            System.out.println(">>> [1/3] Executando migrações do Flyway...");

            flyway.clean();
            flyway.migrate();

            System.out.println(">>> [2/3] Executando as seeders...");
            for (InterfaceSeeder seeder : seeders) {
                System.out.println("------------Executando: " + seeder.nameSeeder() + "------------");
                seeder.run(); // Certifique-se de que o método na sua interface se chama executar()
                System.out.println("------------Concluído: " + seeder.nameSeeder() + "------------");

            }

            System.out.println(">>> [3/3] Setup do banco de dados concluído com sucesso!");
            System.out.println("==============================Finalizando as configurações das informações para rodar o sistema com o banco de dados==============================");
            
            // Encerra a aplicação com sucesso para não deixar o servidor web rodando
            System.exit(0);
        }
    }
}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
    
}

group = "com.japim"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Data JPA & Web / GraphQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    // Banco de Dados
    runtimeOnly("com.mysql:mysql-connector-j")

    // Spring Security & JWT (Ajustado e sem duplicações)
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // GraphQL Scalars
    implementation("com.graphql-java:graphql-java-extended-scalars:24.0")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-graphql-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    
    // Suporte específico do Flyway para MySQL
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<JavaExec>("seeders") {
    group = "database"
    description = "Executa as seeders da aplicação"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.japim.ApiApplication")

    args("--seeders")
}



tasks.register("migration") {
    group = "flyway"
    description = "Cria um novo arquivo de migration do Flyway com timestamp"
    
    doLast {
        // Pega o valor da flag e valida se foi realmente preenchido
        val migrationName = project.findProperty("migration")?.toString()?.trim()
        val versionMigration = project.findProperty("vsMigration")?.toString()?.trim()

        if (migrationName.isNullOrEmpty() || versionMigration.isNullOrEmpty()) {
            throw IllegalArgumentException("ERRO: Você precisa informar o nome da entidade e a versão! Exemplo correto: ./gradlew migration -Pmigration=usuario -PvsMigration=1")
        }
            
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"))
        val fileName = "V${versionMigration}_${timestamp}__${migrationName}_table.sql"
        
        val migrationDir = file("src/main/resources/db/migration")
        if (!migrationDir.exists()) {
            migrationDir.mkdirs()
        }
        
        val newFile = file("$migrationDir/$fileName")
        newFile.writeText("-- Migration para a entidade: $migrationName\n-- Criado em: ${LocalDateTime.now()}\n\n")
        
        println("Migration criada com sucesso: ${newFile.absolutePath}")
    }
}
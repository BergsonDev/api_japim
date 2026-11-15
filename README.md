# Japim API

[Português](#português) | [English](#english)

> **Aviso:** projeto em desenvolvimento. As configurações deste repositório — banco local, credenciais de seed e segredo JWT padrão — são exclusivas para desenvolvimento e **não devem ser usadas em produção**. Consulte o [checklist de produção](#checklist-para-produção).
>
> **Warning:** development project. The settings in this repository — local database, seed credentials, and default JWT secret — are for development only and **must not be used in production**. See the [production checklist](#production-checklist).

---

## Português

API GraphQL do **Instituto Japim**, responsável pelo gerenciamento de usuários, perfis e permissões (RBAC), eventos, presenças e certificados.

O projeto está em desenvolvimento ativo (`0.0.1-SNAPSHOT`) e expõe um único endpoint GraphQL, com interface GraphiQL habilitada para desenvolvimento.

### Stack e versões

| Item | Versão / configuração |
| --- | --- |
| Versão do projeto | `0.0.1-SNAPSHOT` |
| Spring Boot | `4.1.1` |
| Java (toolchain) | `25` |
| Gradle (wrapper) | `9.7.1` |
| Banco de dados | MySQL via Docker (`3307` no host → `3306` no container) |
| Migrations | Flyway (`flyway-mysql`), tabela de controle `japim` |
| API | Spring GraphQL + GraphiQL |
| Segurança | Spring Security stateless + JWT (`jjwt 0.12.5`) |
| Scalars GraphQL | `graphql-java-extended-scalars 24.0` + scalar customizado `LocalDateTime` |
| Persistência | Spring Data JPA / Hibernate (`ddl-auto=validate`) |
| Testes | Spring Boot Test / JUnit (nenhum teste implementado até o momento) |

### Funcionalidades

- **Autenticação**: login via GraphQL, retorno de token JWT (`token_access`).
- **Usuários**: CRUD, consulta do próprio perfil (`me`) e desativação de conta.
- **Roles e permissions**: CRUD e vínculo/desvínculo de permissões a perfis (RBAC).
- **Eventos**: CRUD, inscrição e cancelamento de participantes e voluntários, confirmação do evento, confirmação de presença de participantes e voluntários, encerramento e cancelamento.
- **Certificados**: listagem, consulta por ID, certificados do usuário autenticado e geração do próprio certificado.
- **Status de evento**: cadastro por seeder (`CRI`, `APR`, `CAN`, `ENC`, `EAM`).

### Pré-requisitos

- **JDK 25** instalado e disponível (`java -version`). O build usa toolchain Java 25; não há provisionamento automático de JDK configurado.
- **Docker** e **Docker Compose** para o MySQL.
- **Git** para clonar o repositório.
- Não é necessário instalar o Gradle: use o wrapper (`./gradlew`).
- Portas livres: `8080` (API) e `3307` (MySQL no host).

### Variáveis de ambiente

Definidas em `src/main/resources/application.properties`, com os seguintes defaults. No profile padrão (desenvolvimento) elas são opcionais; no profile `production`, `DB_URL`, `DB_USER`, `DB_PASSWORD` e `JWT_SECRET_KEY` são obrigatórias.

| Variável | Default | Descrição |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3307/japim?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Manaus` | URL JDBC do MySQL |
| `DB_USER` | `root` | Usuário do banco |
| `DB_PASSWORD` | `root` | Senha do banco |
| `PORT` | `8080` | Porta HTTP da API |
| `JWT_SECRET_KEY` | Valor padrão presente no `application.properties` | Chave HMAC do JWT; use no mínimo 32 bytes e um valor próprio fora do ambiente local |
| `SPRING_PROFILES_ACTIVE` | *(vazio)* | Use `production` para ativar o perfil de produção |
| `SEED_ADMIN_PASSWORD` | Valor definido no `UserBasicSeeder` | Senha do usuário `admin` criado pelos seeders de desenvolvimento |
| `SEED_TEST_PASSWORD` | `teste` | Senha do usuário `teste` criado pelos seeders de desenvolvimento |

A expiração do token (`jwt.expiration`) está fixa em `86400000` ms (24 horas).

### Como inicializar

Os passos abaixo usam o profile padrão (desenvolvimento); nenhum profile precisa ser ativado.

1. **Suba o banco de dados**

   ```bash
   docker compose up -d
   docker compose ps
   ```

   O container `database` expõe o MySQL na porta `3307` e cria o banco `japim`.

2. **Prepare o banco (primeira execução)**

   ```bash
   ./gradlew setupDatabase
   ```

   Esse comando executa `flyway.clean()`, aplica todas as migrations e roda os seeders, encerrando a aplicação ao final.

   > **Atenção:** `setupDatabase` **apaga os dados existentes** do schema configurado em `DB_URL`. Use apenas em desenvolvimento.

3. **Inicie a API**

   ```bash
   ./gradlew bootRun
   ```

4. **Acesse**

   - GraphiQL: <http://localhost:8080/graphiql>
   - Endpoint GraphQL: <http://localhost:8080/graphql>

Se você alterar `DB_URL`, `DB_USER` ou `DB_PASSWORD`, exporte as variáveis antes de rodar `setupDatabase` e `bootRun`:

```bash
export DB_URL="jdbc:mysql://localhost:3307/japim?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Manaus"
export DB_USER="root"
export DB_PASSWORD="root"
```

### Comandos disponíveis

Comandos padrão:

| Comando | Descrição |
| --- | --- |
| `./gradlew bootRun` | Inicia a aplicação na porta configurada |
| `./gradlew build` | Compila, executa testes e gera o artefato |
| `./gradlew test` | Executa os testes automatizados |
| `./gradlew clean` | Remove os artefatos de build |

Tasks customizadas definidas em `build.gradle.kts`:

| Comando | Grupo | Descrição |
| --- | --- | --- |
| `./gradlew setupDatabase` | `database` | Limpa o banco (`flyway.clean()`), aplica migrations, executa os seeders e encerra a aplicação. **Destrutivo. Nunca usar em produção.** |
| `./gradlew seeders` | `database` | Executa apenas os seeders, sem limpar o banco, e encerra a aplicação. Não é idempotente: rodar novamente pode duplicar registros ou violar constraints. No profile `production`, cria apenas dados de referência e não cria usuários de teste. |
| `./gradlew migration -Pmigration=<nome> -PvsMigration=<versao>` | `flyway` | Cria um arquivo de migration vazio com timestamp no padrão `V<versao>_<yyyy_MM_dd_HH_mm_ss>__<nome>_table.sql`. |

Exemplo de criação de migration:

```bash
./gradlew migration -Pmigration=usuario -PvsMigration=1
# Cria src/main/resources/db/migration/V1_2026_11_15_10_30_00__usuario_table.sql
```

O sufixo `_table.sql` é fixo na task. Para migrations que não criam tabelas (ex.: `ALTER TABLE`), renomeie o arquivo após a geração.

### Estrutura do projeto

```text
api/
├── build.gradle.kts            # Dependências e tasks customizadas
├── docker-compose.yaml         # MySQL de desenvolvimento
├── gradle/wrapper/             # Gradle Wrapper 9.7.1
├── gradlew / gradlew.bat
├── settings.gradle.kts
└── src/
    ├── main/
    │   ├── java/com/japim/
    │   │   ├── ApiApplication.java
    │   │   ├── command/        # Runners de setupDatabase e seeders
    │   │   ├── controllers/    # Controllers GraphQL
    │   │   ├── dtos/           # DTOs de entrada e saída
    │   │   ├── exceptions/     # Exceções de domínio
    │   │   ├── graphql/        # Configuração de scalars e tratamento de erros
    │   │   ├── models/         # Entidades JPA
    │   │   ├── repositories/   # Repositórios Spring Data
    │   │   ├── security/       # JWT, filtro e configuração do Spring Security
    │   │   ├── seeders/        # Dados iniciais
    │   │   └── services/       # Regras de negócio
    │   └── resources/
    │       ├── application.properties
    │       ├── application-production.properties
    │       ├── db/migration/   # Migrations do Flyway
    │       └── graphql/        # Schemas .graphqls
    └── test/                   # Sem testes no momento
```

### Banco de dados e migrations

- As migrations ficam em `src/main/resources/db/migration`.
- O Flyway roda automaticamente na inicialização da aplicação (`spring.flyway.enabled=true`).
- O Hibernate usa `ddl-auto=validate`: o schema precisa estar de acordo com as entidades.
- A tabela de controle do Flyway é `japim`.
- O Flyway clean está habilitado no profile padrão (`spring.flyway.clean-disabled=false`) para permitir o `setupDatabase` em desenvolvimento; no profile `production` ele é desabilitado.

Migrations existentes (14 arquivos):

| Migration | Objeto criado/alterado |
| --- | --- |
| `V1_2026_10_28_20_59_03__roles_table.sql` | `roles` |
| `V1_2026_10_28_20_59_12__permissions_table.sql` | `permissions` |
| `V1_2026_10_28_21_12_39__role_permissions_table.sql` | `role_permissions` |
| `V1_2026_10_28_21_13_13__users_table.sql` | `users` |
| `V1_2026_10_29_20_50_50__events_table.sql` | `events` |
| `V1_2026_10_29_21_04_03__event_speakers_table.sql` | `event_speakers` |
| `V1_2026_10_29_21_05_24__event_participants_table.sql` | `event_participants` |
| `V1_2026_10_30_19_23_01__event_volunteers_table.sql` | `event_volunteers` |
| `V1_2026_10_30_19_30_43__bundles_table.sql` | `bundles` |
| `V1_2026_10_30_19_31_19__bundle_events_table.sql` | `bundle_events` |
| `V1_2026_10_30_19_31_39__certificates_table.sql` | `certificates` |
| `V1_2026_10_30_19_39_38__event_presences_table.sql` | `event_presences` |
| `V1_2026_11_07_15_03_22__status_events_table.sql` | `status_events` |
| `V1_2026_11_07_15_03_39__add_status_to_events_table.sql` | Coluna de status em `events` |

### Autenticação e RBAC

- `POST /graphql` e `/graphiql` são liberados no filtro HTTP; a autorização é feita por campo com `@PreAuthorize`.
- O login usa o **e-mail** como `username`.
- O token deve ser enviado no header `Authorization: Bearer <token>`.
- No GraphiQL, adicione o header em "Headers":

  ```json
  { "Authorization": "Bearer <TOKEN>" }
  ```

Permissões cadastradas pelos seeders:

- **Usuários**: `users-view`, `users-read`, `users-create`, `users-update`, `users-delete`
- **Roles**: `roles-view`, `roles-read`, `roles-create`, `roles-update`, `roles-delete`
- **Permissions**: `permissions-view`, `permissions-read`, `permissions-create`, `permissions-update`, `permissions-delete`
- **Eventos**: `events-view`, `events-read`, `events-create`, `events-update`, `events-delete`, `events-confirm`, `events-subscribe-participant`, `events-unsubscribe-participant`, `events-subscribe-volunteer`, `events-unsubscribe-volunteer`, `events-confirm-presence`, `events-confirm-presence-volunteers`

Distribuição inicial por perfil:

| Perfil | Permissões atribuídas pelos seeders |
| --- | --- |
| `ADMIN` | Todas as permissões |
| `COORD` | `events-view`, `events-read`, `events-create`, `events-delete`, `events-update` |
| `USER`, `STU`, `SPE`, `DIR` | Nenhuma permissão atribuída inicialmente |

> Observação: o `RolePermissionCoordSeeder` adiciona as permissões ao perfil `COORD`, mas não executa `save` explicitamente na role. Confirme no banco se o vínculo foi persistido antes de contar com essas permissões.

### Dados de seed

Ordem de execução dos seeders:

1. `PermissionBasicSeeder` — permissões de usuários, roles e permissions
2. `RoleBasicSeeder` — perfis básicos
3. `RolePermissionsSeeder` — vincula todas as permissões ao `ADMIN`
4. `UserBasicSeeder` — usuários de desenvolvimento
5. `PermissionEventSeeder` — permissões de eventos (vinculadas ao `ADMIN`)
6. `StatusEventBasicSeeder` — status de evento
7. `RolePermissionCoordSeeder` — permissões de eventos para `COORD`

Perfis criados:

| Sigla | Nome | Descrição |
| --- | --- | --- |
| `ADMIN` | Administrador | Gerencia comportamento e liberação de acesso no sistema |
| `USER` | Usuários | Usuários que não fazem parte do instituto |
| `STU` | Estudantes | Estudantes do instituto |
| `COORD` | Coordenador | Coordenadores |
| `SPE` | Palestrante | Palestrantes |
| `DIR` | Diretor | Diretores |

Status de evento criados:

| Sigla | Nome |
| --- | --- |
| `CRI` | Criado |
| `APR` | Aprovado |
| `CAN` | Cancelado |
| `ENC` | Encerrado |
| `EAM` | Em Andamento |

Usuários criados (somente para desenvolvimento local):

| Nome | E-mail (login) | Perfil |
| --- | --- | --- |
| `admin` | `admin@japim.com` | `ADMIN` |
| `teste` | `teste@japim.com` | `COORD` |

> **Aviso de segurança:** as senhas padrão desses usuários estão definidas em `src/main/java/com/japim/seeders/users/UserBasicSeeder.java` e existem apenas para facilitar o desenvolvimento local. Elas são públicas no repositório e **não devem ser mantidas** em ambientes compartilhados ou de produção. Para desenvolvimento, você pode sobrescrevê-las com `SEED_ADMIN_PASSWORD` e `SEED_TEST_PASSWORD`.

> **Importante:** no profile `production`, o `UserBasicSeeder` não é carregado e o comando `setupDatabase` é bloqueado. O comando `seeders` pode ser executado uma única vez para criar os dados de referência (roles, permissões e status), sem criar os usuários de teste. Ainda assim, remova os usuários de teste de bancos que já tenham sido populados em desenvolvimento.

### Exemplos de uso

**Login**

```graphql
mutation Login {
  login(input: { username: "admin@japim.com", password: "<SENHA_DO_SEEDER>" }) {
    token_access
  }
}
```

**Consultar o próprio perfil**

```graphql
query Me {
  me {
    id
    name
    email
    role {
      name
      initialism
    }
  }
}
```

**Listar eventos**

```graphql
query Events {
  events {
    id
    name
    date_start
    hour_start
    status_event {
      initialism
    }
  }
}
```

**Criar evento** (requer `events-create`)

```graphql
mutation CreateEvent {
  createEvent(input: {
    name: "Workshop de Java"
    short_description: "Introdução ao ecossistema Java"
    description: "Workshop prático sobre Java e Spring."
    is_payment: false
    value: 0
    max_participants: 50
    max_volunteers: 5
    date_start: "2026-12-01"
    hour_start: "09:00:00"
    date_end: "2026-12-01"
    hour_end: "17:00:00"
    workload: 8
    speakers_id: []
  }) {
    id
    name
    status_event {
      initialism
    }
  }
}
```

**Requisição via `curl`**

```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"query":"query { me { id name email } }"}'
```

### Solução de problemas

| Problema | Causa provável | Solução |
| --- | --- | --- |
| `Port 3307 is already allocated` | Outro MySQL ou container usando a porta | Pare o serviço/concorrente ou altere a porta no `docker-compose.yaml` e no `DB_URL` |
| `Port 8080 was already in use` | Outra aplicação na porta | Use `PORT=8081 ./gradlew bootRun` |
| `No matching toolchains found for Java 25` | JDK 25 ausente | Instale o JDK 25 e garanta que o Gradle o encontre |
| `Communications link failure` | Container do MySQL parado ou `DB_URL` incorreto | Rode `docker compose up -d` e confira `docker compose logs db` |
| `Flyway validate failed` / checksum mismatch | Migration já aplicada foi editada | Não edite migrations aplicadas; crie uma nova. Em dev, `setupDatabase` recria o schema (apaga dados) |
| Erro de constraint ao rodar `./gradlew seeders` | Seeders não são idempotentes | Use `./gradlew setupDatabase` para recriar o banco em desenvolvimento |
| GraphiQL não abre | Aplicação não iniciou ou porta diferente | Verifique os logs do `bootRun` e a porta configurada |
| Dados desapareceram | `setupDatabase` executa `flyway.clean()` | Evite o comando fora de ambientes descartáveis |

### Observações e limitações

- Não há implementação de **refresh token**; apenas `token_access` é retornado.
- Não existem testes automatizados em `src/test`.
- A imagem do MySQL no `docker-compose.yaml` não tem tag fixa (`image: mysql`), portanto pode variar conforme a versão `latest` disponível.
- O `HELP.md` está listado no `.gitignore`, apesar de existir no diretório local.
- O schema GraphQL expõe o campo `password` no tipo `User`; evite selecioná-lo nas consultas.
- `setupDatabase` é destrutivo e não deve ser usado em ambientes com dados relevantes.
- O profile `production` bloqueia os comandos destrutivos e desabilita GraphiQL/logs detalhados; os valores padrão de `JWT_SECRET_KEY` e do banco continuam válidos apenas no profile de desenvolvimento.
- O GraphiQL e os logs detalhados de GraphQL/SQL estão habilitados por padrão (desenvolvimento).

### Perfil de produção

Ative o profile `production`:

```bash
SPRING_PROFILES_ACTIVE=production \
JWT_SECRET_KEY="<chave-forte-com-32-bytes-ou-mais>" \
DB_URL="jdbc:mysql://<host>:3306/japim?allowPublicKeyRetrieval=true&useSSL=false" \
DB_USER="<usuario>" \
DB_PASSWORD="<senha>" \
./gradlew bootRun
```

Com esse profile:

- `JWT_SECRET_KEY`, `DB_URL`, `DB_USER` e `DB_PASSWORD` são obrigatórios: a aplicação falha ao iniciar sem eles.
- O `JWT_SECRET_KEY` não pode ser igual ao valor padrão do repositório.
- `setupDatabase` é bloqueado.
- `seeders` pode ser executado uma única vez para popular dados de referência (roles, permissões e status); o `UserBasicSeeder` não é carregado e nenhum usuário de teste é criado.
- GraphiQL, SQL logging e detalhes de erro do GraphQL são desabilitados.
- O `flyway.clean` fica desabilitado.

As configurações ficam em `src/main/resources/application-production.properties`.

Para popular os dados de referência em produção, execute o `seeders` uma única vez com o profile ativo:

```bash
SPRING_PROFILES_ACTIVE=production \
JWT_SECRET_KEY="<chave-forte-com-32-bytes-ou-mais>" \
DB_URL="jdbc:mysql://<host>:3306/japim?allowPublicKeyRetrieval=true&useSSL=false" \
DB_USER="<usuario>" \
DB_PASSWORD="<senha>" \
./gradlew seeders
```

#### Impacto no build e na execução

O profile é uma configuração de **runtime**. O `./gradlew build` não lê `application-production.properties` e não gera um artefato diferente: o mesmo `jar` contém os dois arquivos e a escolha acontece na inicialização.

| Comando | Sem profile | Com `SPRING_PROFILES_ACTIVE=production` |
| --- | --- | --- |
| `./gradlew build` | Igual | Igual — profiles não são avaliados no build |
| `./gradlew bootRun` | Configuração de desenvolvimento | Exige as quatro variáveis e falha sem elas |
| `./gradlew setupDatabase` | Funciona | Bloqueado |
| `./gradlew seeders` | Todos os seeders, incluindo usuários de teste | Apenas dados de referência, sem usuários de teste |
| `./gradlew test` | Igual | Se executado com o profile, também exige as variáveis |

Formas alternativas de ativar o profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=production'
```

Ou configure `SPRING_PROFILES_ACTIVE=production` na run configuration da IDE.

> **Cuidado:** não deixe `SPRING_PROFILES_ACTIVE=production` exportado globalmente no shell. Comandos locais como `./gradlew bootRun`, `setupDatabase` e `seeders` passarão a exigir as variáveis de produção e o comportamento de desenvolvimento será perdido.

### Checklist para produção

- [x] Ativar `SPRING_PROFILES_ACTIVE=production`.
- [x] Definir um `JWT_SECRET_KEY` forte, exclusivo e com pelo menos 32 bytes.
- [x] Definir `DB_URL`, `DB_USER` e `DB_PASSWORD` com um usuário dedicado do MySQL.
- [x] Bloquear `setupDatabase` em produção.
- [x] Impedir a criação dos usuários de teste no profile `production`.
- [ ] Executar `./gradlew seeders` uma única vez em produção para criar os dados de referência (roles, permissões e status).
- [x] Desabilitar GraphiQL e logs detalhados via profile de produção.
- [ ] Remover os usuários de teste `admin` e `teste` de bancos já populados em desenvolvimento.
- [ ] Servir a API atrás de HTTPS, revisar portas expostas e configurar o firewall.
- [ ] Configurar backup e política de retenção do banco de dados.
- [ ] Remover ou proteger o campo `password` do schema GraphQL.
- [ ] Adicionar testes automatizados e pipeline de CI antes de publicar.
- [ ] Fixar a versão da imagem do MySQL no `docker-compose.yaml` (ex.: `mysql:8.4`).

---

## English

GraphQL API for **Instituto Japim**, responsible for managing users, roles and permissions (RBAC), events, attendance, and certificates.

The project is under active development (`0.0.1-SNAPSHOT`) and exposes a single GraphQL endpoint, with GraphiQL enabled for development.

### Stack and versions

| Item | Version / configuration |
| --- | --- |
| Project version | `0.0.1-SNAPSHOT` |
| Spring Boot | `4.1.1` |
| Java (toolchain) | `25` |
| Gradle (wrapper) | `9.7.1` |
| Database | MySQL via Docker (`3307` on host → `3306` in container) |
| Migrations | Flyway (`flyway-mysql`), control table `japim` |
| API | Spring GraphQL + GraphiQL |
| Security | Spring Security stateless + JWT (`jjwt 0.12.5`) |
| GraphQL scalars | `graphql-java-extended-scalars 24.0` + custom `LocalDateTime` scalar |
| Persistence | Spring Data JPA / Hibernate (`ddl-auto=validate`) |
| Tests | Spring Boot Test / JUnit (no tests implemented yet) |

### Features

- **Authentication**: GraphQL login returning a JWT (`token_access`).
- **Users**: CRUD, current user profile (`me`), and account deactivation.
- **Roles and permissions**: CRUD and permission assignment/removal for roles (RBAC).
- **Events**: CRUD, participant and volunteer subscription/unsubscription, event confirmation, participant and volunteer attendance confirmation, finishing and cancellation.
- **Certificates**: listing, lookup by ID, authenticated user's certificates, and self-service certificate generation.
- **Event status**: seeded values (`CRI`, `APR`, `CAN`, `ENC`, `EAM`).

### Prerequisites

- **JDK 25** installed and available (`java -version`). The build uses a Java 25 toolchain; no automatic JDK provisioning is configured.
- **Docker** and **Docker Compose** for MySQL.
- **Git** to clone the repository.
- No local Gradle installation is required: use the wrapper (`./gradlew`).
- Free ports: `8080` (API) and `3307` (MySQL on host).

### Environment variables

Defined in `src/main/resources/application.properties`, with the following defaults. In the default (development) profile they are optional; under the `production` profile, `DB_URL`, `DB_USER`, `DB_PASSWORD`, and `JWT_SECRET_KEY` are required.

| Variable | Default | Description |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3307/japim?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Manaus` | MySQL JDBC URL |
| `DB_USER` | `root` | Database user |
| `DB_PASSWORD` | `root` | Database password |
| `PORT` | `8080` | API HTTP port |
| `JWT_SECRET_KEY` | Default value in `application.properties` | JWT HMAC key; use at least 32 bytes and your own value outside local environments |
| `SPRING_PROFILES_ACTIVE` | *(empty)* | Use `production` to enable the production profile |
| `SEED_ADMIN_PASSWORD` | Value defined in `UserBasicSeeder` | Password for the `admin` user created by the development seeders |
| `SEED_TEST_PASSWORD` | `teste` | Password for the `teste` user created by the development seeders |

Token expiration (`jwt.expiration`) is fixed at `86400000` ms (24 hours).

### Getting started

The steps below use the default (development) profile; no profile needs to be enabled.

1. **Start the database**

   ```bash
   docker compose up -d
   docker compose ps
   ```

   The `database` container exposes MySQL on port `3307` and creates the `japim` database.

2. **Prepare the database (first run)**

   ```bash
   ./gradlew setupDatabase
   ```

   This command runs `flyway.clean()`, applies all migrations, runs the seeders, and shuts the application down.

   > **Warning:** `setupDatabase` **deletes existing data** in the schema configured by `DB_URL`. Use it for development only.

3. **Start the API**

   ```bash
   ./gradlew bootRun
   ```

4. **Access**

   - GraphiQL: <http://localhost:8080/graphiql>
   - GraphQL endpoint: <http://localhost:8080/graphql>

If you change `DB_URL`, `DB_USER`, or `DB_PASSWORD`, export the variables before running `setupDatabase` and `bootRun`:

```bash
export DB_URL="jdbc:mysql://localhost:3307/japim?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Manaus"
export DB_USER="root"
export DB_PASSWORD="root"
```

### Available commands

Standard commands:

| Command | Description |
| --- | --- |
| `./gradlew bootRun` | Starts the application on the configured port |
| `./gradlew build` | Compiles, runs tests, and builds the artifact |
| `./gradlew test` | Runs the automated tests |
| `./gradlew clean` | Removes build artifacts |

Custom tasks defined in `build.gradle.kts`:

| Command | Group | Description |
| --- | --- | --- |
| `./gradlew setupDatabase` | `database` | Cleans the database (`flyway.clean()`), applies migrations, runs the seeders, and shuts down the application. **Destructive. Never use in production.** |
| `./gradlew seeders` | `database` | Runs only the seeders, without cleaning the database, then shuts down the application. It is not idempotent: running it again may duplicate records or violate constraints. Under the `production` profile, it creates only reference data and no test users. |
| `./gradlew migration -Pmigration=<name> -PvsMigration=<version>` | `flyway` | Creates an empty migration file with a timestamp in the format `V<version>_<yyyy_MM_dd_HH_mm_ss>__<name>_table.sql`. |

Migration creation example:

```bash
./gradlew migration -Pmigration=usuario -PvsMigration=1
# Creates src/main/resources/db/migration/V1_2026_11_15_10_30_00__usuario_table.sql
```

The `_table.sql` suffix is hardcoded in the task. For migrations that do not create tables (e.g., `ALTER TABLE`), rename the generated file.

### Project structure

```text
api/
├── build.gradle.kts            # Dependencies and custom tasks
├── docker-compose.yaml         # Development MySQL
├── gradle/wrapper/             # Gradle Wrapper 9.7.1
├── gradlew / gradlew.bat
├── settings.gradle.kts
└── src/
    ├── main/
    │   ├── java/com/japim/
    │   │   ├── ApiApplication.java
    │   │   ├── command/        # setupDatabase and seeders runners
    │   │   ├── controllers/    # GraphQL controllers
    │   │   ├── dtos/           # Input and output DTOs
    │   │   ├── exceptions/     # Domain exceptions
    │   │   ├── graphql/        # Scalar configuration and error handling
    │   │   ├── models/         # JPA entities
    │   │   ├── repositories/   # Spring Data repositories
    │   │   ├── security/       # JWT, filter, and Spring Security configuration
    │   │   ├── seeders/        # Initial data
    │   │   └── services/       # Business rules
    │   └── resources/
    │       ├── application.properties
    │       ├── application-production.properties
    │       ├── db/migration/   # Flyway migrations
    │       └── graphql/        # .graphqls schemas
    └── test/                   # No tests at the moment
```

### Database and migrations

- Migrations live in `src/main/resources/db/migration`.
- Flyway runs automatically on application startup (`spring.flyway.enabled=true`).
- Hibernate uses `ddl-auto=validate`: the schema must match the entities.
- The Flyway control table is `japim`.
- Flyway clean is enabled in the default profile (`spring.flyway.clean-disabled=false`) to support `setupDatabase` in development; it is disabled under the `production` profile.

Existing migrations (14 files):

| Migration | Object created/changed |
| --- | --- |
| `V1_2026_10_28_20_59_03__roles_table.sql` | `roles` |
| `V1_2026_10_28_20_59_12__permissions_table.sql` | `permissions` |
| `V1_2026_10_28_21_12_39__role_permissions_table.sql` | `role_permissions` |
| `V1_2026_10_28_21_13_13__users_table.sql` | `users` |
| `V1_2026_10_29_20_50_50__events_table.sql` | `events` |
| `V1_2026_10_29_21_04_03__event_speakers_table.sql` | `event_speakers` |
| `V1_2026_10_29_21_05_24__event_participants_table.sql` | `event_participants` |
| `V1_2026_10_30_19_23_01__event_volunteers_table.sql` | `event_volunteers` |
| `V1_2026_10_30_19_30_43__bundles_table.sql` | `bundles` |
| `V1_2026_10_30_19_31_19__bundle_events_table.sql` | `bundle_events` |
| `V1_2026_10_30_19_31_39__certificates_table.sql` | `certificates` |
| `V1_2026_10_30_19_39_38__event_presences_table.sql` | `event_presences` |
| `V1_2026_11_07_15_03_22__status_events_table.sql` | `status_events` |
| `V1_2026_11_07_15_03_39__add_status_to_events_table.sql` | Status column in `events` |

### Authentication and RBAC

- `POST /graphql` and `/graphiql` are permitted at the HTTP filter level; authorization is enforced per field with `@PreAuthorize`.
- Login uses the **email** as `username`.
- The token must be sent in the `Authorization: Bearer <token>` header.
- In GraphiQL, add the header under "Headers":

  ```json
  { "Authorization": "Bearer <TOKEN>" }
  ```

Permissions created by the seeders:

- **Users**: `users-view`, `users-read`, `users-create`, `users-update`, `users-delete`
- **Roles**: `roles-view`, `roles-read`, `roles-create`, `roles-update`, `roles-delete`
- **Permissions**: `permissions-view`, `permissions-read`, `permissions-create`, `permissions-update`, `permissions-delete`
- **Events**: `events-view`, `events-read`, `events-create`, `events-update`, `events-delete`, `events-confirm`, `events-subscribe-participant`, `events-unsubscribe-participant`, `events-subscribe-volunteer`, `events-unsubscribe-volunteer`, `events-confirm-presence`, `events-confirm-presence-volunteers`

Initial assignment per role:

| Role | Permissions assigned by seeders |
| --- | --- |
| `ADMIN` | All permissions |
| `COORD` | `events-view`, `events-read`, `events-create`, `events-delete`, `events-update` |
| `USER`, `STU`, `SPE`, `DIR` | No permissions assigned initially |

> Note: `RolePermissionCoordSeeder` adds the permissions to the `COORD` role but does not explicitly call `save` on the role. Verify the assignment in the database before relying on these permissions.

### Seed data

Seeder execution order:

1. `PermissionBasicSeeder` — user, role, and permission permissions
2. `RoleBasicSeeder` — basic roles
3. `RolePermissionsSeeder` — assigns all permissions to `ADMIN`
4. `UserBasicSeeder` — development users
5. `PermissionEventSeeder` — event permissions (assigned to `ADMIN`)
6. `StatusEventBasicSeeder` — event statuses
7. `RolePermissionCoordSeeder` — event permissions for `COORD`

Created roles:

| Initialism | Name | Description |
| --- | --- | --- |
| `ADMIN` | Administrador | Manages system behavior and access control |
| `USER` | Usuários | Users who are not part of the institute |
| `STU` | Estudantes | Institute students |
| `COORD` | Coordenador | Coordinators |
| `SPE` | Palestrante | Speakers |
| `DIR` | Diretor | Directors |

Created event statuses:

| Initialism | Name |
| --- | --- |
| `CRI` | Criado |
| `APR` | Aprovado |
| `CAN` | Cancelado |
| `ENC` | Encerrado |
| `EAM` | Em Andamento |

Created users (local development only):

| Name | Email (login) | Role |
| --- | --- | --- |
| `admin` | `admin@japim.com` | `ADMIN` |
| `teste` | `teste@japim.com` | `COORD` |

> **Security warning:** the default passwords for these users are defined in `src/main/java/com/japim/seeders/users/UserBasicSeeder.java` and exist only to simplify local development. They are public in the repository and **must not be kept** in shared or production environments. For development, you can override them with `SEED_ADMIN_PASSWORD` and `SEED_TEST_PASSWORD`.

> **Important:** under the `production` profile, `UserBasicSeeder` is not loaded and the `setupDatabase` command is blocked. The `seeders` command can be run once to create reference data (roles, permissions, and statuses), without creating test users. Still, remove the test users from any database that was already populated in development.

### Usage examples

**Login**

```graphql
mutation Login {
  login(input: { username: "admin@japim.com", password: "<PASSWORD_FROM_SEEDER>" }) {
    token_access
  }
}
```

**Get the current user profile**

```graphql
query Me {
  me {
    id
    name
    email
    role {
      name
      initialism
    }
  }
}
```

**List events**

```graphql
query Events {
  events {
    id
    name
    date_start
    hour_start
    status_event {
      initialism
    }
  }
}
```

**Create an event** (requires `events-create`)

```graphql
mutation CreateEvent {
  createEvent(input: {
    name: "Workshop de Java"
    short_description: "Introdução ao ecossistema Java"
    description: "Workshop prático sobre Java e Spring."
    is_payment: false
    value: 0
    max_participants: 50
    max_volunteers: 5
    date_start: "2026-12-01"
    hour_start: "09:00:00"
    date_end: "2026-12-01"
    hour_end: "17:00:00"
    workload: 8
    speakers_id: []
  }) {
    id
    name
    status_event {
      initialism
    }
  }
}
```

**Request with `curl`**

```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"query":"query { me { id name email } }"}'
```

### Troubleshooting

| Problem | Likely cause | Solution |
| --- | --- | --- |
| `Port 3307 is already allocated` | Another MySQL or container using the port | Stop the conflicting service or change the port in `docker-compose.yaml` and `DB_URL` |
| `Port 8080 was already in use` | Another application on the port | Run `PORT=8081 ./gradlew bootRun` |
| `No matching toolchains found for Java 25` | JDK 25 is missing | Install JDK 25 and make sure Gradle can find it |
| `Communications link failure` | MySQL container stopped or wrong `DB_URL` | Run `docker compose up -d` and check `docker compose logs db` |
| `Flyway validate failed` / checksum mismatch | An applied migration was edited | Do not edit applied migrations; create a new one. In dev, `setupDatabase` recreates the schema (data loss) |
| Constraint error when running `./gradlew seeders` | Seeders are not idempotent | Use `./gradlew setupDatabase` to recreate the database in development |
| GraphiQL does not open | Application did not start or is on another port | Check `bootRun` logs and the configured port |
| Data disappeared | `setupDatabase` runs `flyway.clean()` | Avoid the command outside disposable environments |

### Notes and limitations

- There is no **refresh token** implementation; only `token_access` is returned.
- There are no automated tests in `src/test`.
- The MySQL image in `docker-compose.yaml` has no fixed tag (`image: mysql`), so it may vary with the available `latest` version.
- `HELP.md` is listed in `.gitignore` even though it exists in the local directory.
- The GraphQL schema exposes the `password` field on the `User` type; avoid selecting it in queries.
- `setupDatabase` is destructive and must not be used in environments with relevant data.
- The `production` profile blocks destructive commands and disables GraphiQL/detailed logs; the default `JWT_SECRET_KEY` and database values remain valid only in the development profile.
- GraphiQL and detailed GraphQL/SQL logging are enabled by default (development).

### Production profile

Enable the `production` profile:

```bash
SPRING_PROFILES_ACTIVE=production \
JWT_SECRET_KEY="<strong-key-with-32-bytes-or-more>" \
DB_URL="jdbc:mysql://<host>:3306/japim?allowPublicKeyRetrieval=true&useSSL=false" \
DB_USER="<user>" \
DB_PASSWORD="<password>" \
./gradlew bootRun
```

With this profile:

- `JWT_SECRET_KEY`, `DB_URL`, `DB_USER`, and `DB_PASSWORD` are required: the application fails to start without them.
- `JWT_SECRET_KEY` must not be the repository default.
- `setupDatabase` is blocked.
- `seeders` can be run once to populate reference data (roles, permissions, and statuses); `UserBasicSeeder` is not loaded and no test users are created.
- GraphiQL, SQL logging, and GraphQL error details are disabled.
- `flyway.clean` is disabled.

The settings live in `src/main/resources/application-production.properties`.

To populate reference data in production, run `seeders` once with the profile enabled:

```bash
SPRING_PROFILES_ACTIVE=production \
JWT_SECRET_KEY="<strong-key-with-32-bytes-or-more>" \
DB_URL="jdbc:mysql://<host>:3306/japim?allowPublicKeyRetrieval=true&useSSL=false" \
DB_USER="<user>" \
DB_PASSWORD="<password>" \
./gradlew seeders
```

#### Build and runtime impact

The profile is a **runtime** setting. `./gradlew build` does not read `application-production.properties` and does not produce a different artifact: the same `jar` contains both files, and the choice happens at startup.

| Command | Without the profile | With `SPRING_PROFILES_ACTIVE=production` |
| --- | --- | --- |
| `./gradlew build` | Same | Same — profiles are not evaluated at build time |
| `./gradlew bootRun` | Development configuration | Requires the four variables and fails without them |
| `./gradlew setupDatabase` | Works | Blocked |
| `./gradlew seeders` | All seeders, including test users | Reference data only, no test users |
| `./gradlew test` | Same | If run with the profile, it also requires the variables |

Alternative ways to enable the profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=production'
```

Or set `SPRING_PROFILES_ACTIVE=production` in your IDE run configuration.

> **Caution:** do not export `SPRING_PROFILES_ACTIVE=production` globally in your shell. Local commands such as `./gradlew bootRun`, `setupDatabase`, and `seeders` will then require the production variables and the development behavior will be lost.

### Production checklist

- [x] Enable `SPRING_PROFILES_ACTIVE=production`.
- [x] Set a strong, unique `JWT_SECRET_KEY` with at least 32 bytes.
- [x] Set `DB_URL`, `DB_USER`, and `DB_PASSWORD` with a dedicated MySQL user.
- [x] Block `setupDatabase` in production.
- [x] Prevent test user creation under the `production` profile.
- [ ] Run `./gradlew seeders` once in production to create reference data (roles, permissions, and statuses).
- [x] Disable GraphiQL and detailed logs through the production profile.
- [ ] Remove the `admin` and `teste` test users from databases already populated in development.
- [ ] Serve the API over HTTPS, review exposed ports, and configure a firewall.
- [ ] Configure database backups and a retention policy.
- [ ] Remove or protect the `password` field in the GraphQL schema.
- [ ] Add automated tests and a CI pipeline before publishing.
- [ ] Pin the MySQL image version in `docker-compose.yaml` (e.g., `mysql:8.4`).

# 🚀 M42Hub - API

<p align="center">
  <img src="https://raw.githubusercontent.com/m42hub/m42hub-client/main/public/logo.png" alt="M42Hub Logo" width="120" height="120">
</p>

<p align="center">
  <strong>A engine que conecta conhecimentos para criar algo incrível</strong>
</p>

<p align="center">
  <a href="https://github.com/m42hub/m42hub-api">
    <img src="https://img.shields.io/badge/Open%20Source-💚-brightgreen" alt="Open Source">
  </a>
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.5.3-green" alt="Spring Boot 3.5.3">
  </a>
  <a href="https://openjdk.java.net/projects/jdk/21/">
    <img src="https://img.shields.io/badge/Java-21-orange" alt="Java 21">
  </a>
  <a href="https://www.postgresql.org/">
    <img src="https://img.shields.io/badge/PostgreSQL-15+-blue" alt="PostgreSQL">
  </a>
  <a href="https://jwt.io/">
    <img src="https://img.shields.io/badge/JWT-Authentication-purple" alt="JWT">
  </a>
</p>

---

## 📝 Sobre o Projeto

A **M42Hub API** é o backend robusto e escalável que alimenta a plataforma open-source M42Hub. Esta API REST fornece todos os serviços necessários para conectar pessoas com diferentes habilidades e conhecimentos, permitindo a colaboração em projetos inovadores através de endpoints seguros e bem documentados.

### 🎯 Missão

Fornecer uma infraestrutura confiável e performática que suporte a visão da plataforma M42Hub: criar um espaço onde talentos diversos se encontram para dar vida a ideias de todos os tipos e tamanhos.

### ✨ Principais Funcionalidades

- 🔐 **Autenticação JWT**: Sistema completo de login/registro com tokens seguros
- 👥 **Gestão de Usuários**: CRUD completo com sistema de permissões baseado em roles
- 📊 **Gestão de Projetos**: Criação, listagem, filtros avançados e categorização
- 🏷️ **Sistema de Tags**: Tópicos, ferramentas e complexidade para classificação
- 👥 **Colaboração**: Sistema de aplicação e aprovação de membros em projetos
- 🛡️ **Autorização Granular**: Controle de acesso baseado em permissões específicas
- 📄 **Paginação Inteligente**: Listagens otimizadas com filtros dinâmicos
- 🗄️ **Migrações Automáticas**: Versionamento de banco de dados com Flyway
- 🎥 **Desenvolvimento Transparente**: Construído ao vivo nas [lives do BetGrave](https://www.youtube.com/@betgrave)!

## 🛠️ Tecnologias Utilizadas

### Backend
- **[Spring Boot 3.5.3](https://spring.io/projects/spring-boot)** - Framework principal
- **[Java 21](https://openjdk.java.net/projects/jdk/21/)** - Linguagem de programação
- **[Spring Security](https://spring.io/projects/spring-security)** - Autenticação e autorização
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)** - Persistência de dados
- **[JWT](https://jwt.io/)** - Tokens de autenticação
- **[Lombok](https://projectlombok.org/)** - Redução de boilerplate

### Banco de Dados
- **[PostgreSQL](https://www.postgresql.org/)** - Banco de dados principal
- **[Flyway](https://flywaydb.org/)** - Migrações de banco de dados

### Infraestrutura
- **[Docker](https://www.docker.com/)** - Containerização
- **[Docker Compose](https://docs.docker.com/compose/)** - Orquestração de containers
- **[Maven](https://maven.apache.org/)** - Gerenciamento de dependências

## 🚀 Começando

### Pré-requisitos

- **Java 21** ou superior
- **Maven 3.8+**
- **PostgreSQL 15+** (ou Docker para executar via containers)
- **Git**

### 📦 Instalação Local

1. **Clone o repositório**
   ```bash
   git clone https://github.com/m42hub/m42hub-api.git
   cd m42hub-api
   ```

2. **Configure o banco de dados**
   
   Crie um banco PostgreSQL e configure as variáveis de ambiente:
   ```bash
   cp example.env .env
   # Edite o arquivo .env com suas configurações
   ```

3. **Execute as migrações**
   ```bash
   mvn flyway:migrate
   ```

4. **Compile e execute**
   ```bash
   mvn spring-boot:run
   ```

5. **Acesse a API**
   
   A API estará disponível em `http://localhost:8080`

### 🐳 Execução com Docker

1. **Configuração do ambiente**
   ```bash
   cp example.env .env
   # Ajuste as configurações se necessário
   ```

2. **Build e execução com Docker Compose**
   ```bash
   mvn clean package -DskipTests
   docker-compose up --build
   ```

3. **Acesse a API**
   
   A API estará disponível em `http://localhost:8084`

### 🏗️ Build para Produção

```bash
# Compilação completa com testes
mvn clean package

# Build da imagem Docker
docker build -t m42hub-api .

# Execução em produção
docker run -d \
  --name m42hub-api \
  -p 8080:8080 \
  --env-file .env \
  m42hub-api
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/m42hub/m42hub_api/
│   │   ├── config/              # Configurações e segurança
│   │   │   ├── SecurityConfig.java
│   │   │   ├── TokenService.java
│   │   │   ├── JWTUserData.java
│   │   │   └── ApplicationControllerAdvice.java
│   │   ├── exceptions/          # Exceções customizadas
│   │   ├── project/             # Módulo de projetos
│   │   │   ├── controller/      # Controllers REST
│   │   │   ├── dto/             # DTOs de request/response
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── mapper/          # Mapeadores de dados
│   │   │   ├── repository/      # Repositórios JPA
│   │   │   ├── service/         # Lógica de negócio
│   │   │   └── specification/   # Especificações para filtros
│   │   ├── user/                # Módulo de usuários
│   │   │   ├── controller/      # Controllers de auth/user
│   │   │   ├── dto/             # DTOs de request/response
│   │   │   ├── entity/          # Entidades de usuário
│   │   │   ├── mapper/          # Mapeadores
│   │   │   ├── repository/      # Repositórios
│   │   │   └── service/         # Serviços de usuário
│   │   └── M42hubApiApplication.java
│   └── resources/
│       ├── application.yml      # Configurações da aplicação
│       └── db/migration/        # Scripts de migração Flyway
├── test/                        # Testes unitários e integração
└── target/                      # Artefatos compilados
```

## 🔌 Principais Endpoints

### Autenticação (`/api/v1/auth/`)
- `POST /login` - Login de usuário
- `POST /register` - Registro de usuário
- `GET /validate` - Validação de token
- `POST /logout` - Logout

### Usuários (`/api/v1/user`)
- `GET /` - Listar usuários (admin)
- `GET /{id}` - Buscar usuário por ID (admin)
- `POST /` - Criar usuário (admin)
- `GET /me` - Dados do usuário logado

### Roles e Permissões
- `GET /api/v1/user/permission` - Listar permissões (admin)
- `GET /api/v1/user/permission/{id}` - Buscar permissão por ID (admin)
- `POST /api/v1/user/permission` - Criar permissão (admin)
- `GET /api/v1/user/system-role` - Listar roles do sistema (admin)
- `GET /api/v1/user/system-role/{id}` - Buscar role por ID (admin)
- `POST /api/v1/user/system-role` - Criar role (admin)
- `PATCH /api/v1/user/system-role/permissions/{id}` - Alterar permissões de role (admin)

### Projetos (`/api/v1/project`)
- `GET /` - Listar projetos (com filtros e paginação)
- `POST /` - Criar projeto
- `GET /{id}` - Detalhes do projeto
- `PATCH /{id}` - Atualizar projeto
- `PATCH /unfilled-roles/{id}` - Alterar vagas não preenchidas

### Membros (`/api/v1/project/member`)
- `GET /` - Listar membros (admin)
- `GET /{id}` - Buscar membro por ID (admin)
- `POST /` - Criar membro (admin)
- `POST /apply` - Aplicar para projeto
- `PATCH /approve/{id}` - Aprovar aplicação
- `PATCH /reject/{id}` - Rejeitar aplicação

### Status e Categorização
- `GET /api/v1/project/status` - Status de projetos
- `POST /api/v1/project/status` - Criar status (admin)
- `GET /api/v1/project/complexity` - Níveis de complexidade
- `POST /api/v1/project/complexity` - Criar complexidade (admin)

### Recursos de Projeto
- `GET /api/v1/project/topic` - Tópicos disponíveis
- `POST /api/v1/project/topic` - Criar tópico (admin)
- `PATCH /api/v1/project/topic/color/{id}` - Alterar cor do tópico (admin)
- `GET /api/v1/project/tool` - Ferramentas disponíveis
- `POST /api/v1/project/tool` - Criar ferramenta (admin)
- `PATCH /api/v1/project/tool/color/{id}` - Alterar cor da ferramenta (admin)
- `GET /api/v1/project/role` - Roles de projeto
- `POST /api/v1/project/role` - Criar role de projeto (admin)

### Status de Membros
- `GET /api/v1/project/member/status` - Status de membros
- `POST /api/v1/project/member/status` - Criar status de membro (admin)

## 🔐 Sistema de Autenticação

A API utiliza **JWT (JSON Web Tokens)** para autenticação. O fluxo funciona da seguinte forma:

1. **Registro/Login**: O usuário se autentica e recebe um token JWT
2. **Cookie Seguro**: O token é armazenado em um cookie HttpOnly
3. **Autorização**: Cada requisição é validada através do token
4. **Permissões**: Sistema granular baseado em roles e permissões específicas

### Exemplo de uso:
```bash
# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "usuario", "password": "senha"}'

# Usar recursos protegidos (o cookie é enviado automaticamente)
curl -X GET http://localhost:8080/api/v1/user/me \
  --cookie "session=seu_jwt_token"
```

## 🤝 Como Contribuir

Adoramos contribuições da comunidade! O processo é simples e direto:

### 📋 Processo de Contribuição

1. **Fork o repositório**
   - Acesse [github.com/m42hub/m42hub-api](https://github.com/m42hub/m42hub-api)
   - Clique em "Fork" para criar sua cópia do projeto

2. **Clone seu fork**
   ```bash
   git clone https://github.com/SEU_USUARIO/m42hub-api.git
   cd m42hub-api
   ```

3. **Configure o upstream**
   ```bash
   git remote add upstream https://github.com/m42hub/m42hub-api.git
   ```

4. **Crie uma branch para sua feature**
   ```bash
   git checkout -b feature/minha-feature
   ```

5. **Faça suas alterações**
   - Implemente sua feature ou correção
   - Siga os padrões de código estabelecidos
   - Adicione testes se necessário

6. **Execute os testes**
   ```bash
   mvn test
   ```

7. **Commit suas mudanças**
   ```bash
   git add .
   git commit -m "feat: adiciona nova funcionalidade"
   ```


8. **Push para seu fork**
   ```bash
   git push origin feature/minha-feature
   ```
   
9. **Mantenha seu fork atualizado**
   ```bash
   git fetch upstream
   git pull upstream main
   ```

10. **Abra um Pull Request**
    - Vá para seu fork no GitHub
    - Clique em "New Pull Request"
    - **Aponte diretamente para a branch `main`** do repositório original
    - Descreva suas mudanças detalhadamente

### 🎯 Tipos de Contribuição

#### � Reportar Bugs
- Use o [sistema de issues](https://github.com/m42hub/m42hub-api/issues)
- Use o template de bug report
- Inclua logs, stack traces e steps para reproduzir
- Adicione labels apropriadas

#### 💡 Sugerir Features
- Abra uma issue com a tag `enhancement`
- Use o template de feature request
- Explique o problema que a feature resolve
- Forneça exemplos de uso e mockups se aplicável

#### 🔧 Contribuir com Código
- **Correções de bug**: Sempre bem-vindas
- **Novas features**: Discuta primeiro em uma issue
- **Melhorias de performance**: Inclua benchmarks
- **Documentação**: Ajude a manter tudo atualizado
- **Testes**: Aumente a cobertura e qualidade

### 📋 Padrões de Código

#### **Convenções Java**
```java
// ✅ Bom
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}

// ❌ Evitar
public class userservice {
    public user getUser(long id) { ... }
}
```

#### **Commits Semânticos**
```bash
# Tipos aceitos
feat: nova funcionalidade
fix: correção de bug
docs: mudanças na documentação
style: formatação, ponto e vírgula, etc
refactor: refatoração de código
test: adição ou correção de testes
chore: tarefas de manutenção

# Exemplos
feat: adiciona endpoint para buscar projetos por categoria
fix: corrige validação de email no registro
docs: atualiza README com novas instruções
test: adiciona testes para ProjectService
```

#### **Estrutura de Branch**
```bash
# Nomenclatura
feature/nome-da-feature    # Nova funcionalidade
fix/nome-do-bug           # Correção de bug
docs/nome-da-doc          # Documentação
test/nome-do-teste        # Testes

# Exemplos
feature/user-profile-picture
fix/jwt-token-expiration
docs/api-documentation
test/project-controller-integration
```

### 🧪 Qualidade e Testes

#### **Cobertura de Testes**
- Mantenha cobertura mínima de **80%**
- Teste casos de sucesso e falha
- Inclua testes de integração para endpoints

#### **Checklist de PR**
- [ ] Código compila sem warnings
- [ ] Todos os testes passam
- [ ] Cobertura de testes mantida
- [ ] Documentação atualizada
- [ ] Commits seguem padrão semântico
- [ ] Branch atualizada com main

### 🎥 Processo de Revisão

- **Pull Requests são revisados ao vivo** no canal [BetGrave](https://www.youtube.com/@betgrave)! 📺
- **Horário das lives**: Todos os dias as 20:00 (horário de Brasília)

## 🧪 Executando Testes

A API possui testes unitários implementados para garantir qualidade e confiabilidade dos serviços.

### Estrutura de Testes Atual

O projeto atualmente contém:
- **Testes de Service**: Foco nos serviços de negócio com mocks
- **TestUtils**: Classe utilitária para criação de objetos de teste
- **Testes Unitários**: Usando JUnit 5, Mockito e AssertJ

### Comandos de Teste

```bash
# Executar todos os testes
mvn test

# Executar teste específico
mvn test -Dtest=PermissionServiceTest

# Executar testes com relatório detalhado
mvn test -Dtest.verbose=true

# Compilar sem executar testes
mvn clean package -DskipTests
```

### Testes Implementados

#### **Serviços de Usuário**
- `PermissionServiceTest` - Testa gestão de permissões
- `SystemRoleServiceTest` - Testa gestão de roles do sistema

#### **Serviços de Projeto**
- `ComplexityServiceTest` - Testa níveis de complexidade
- `MemberServiceTest` - Testa gestão de membros
- `MemberStatusServiceTest` - Testa status de membros
- `ProjectServiceTest` - Testa gestão de projetos
- `RoleServiceTest` - Testa roles de projeto
- `StatusServiceTest` - Testa status de projetos
- `ToolServiceTest` - Testa ferramentas
- `TopicServiceTest` - Testa tópicos

### Exemplo de Teste Atual

Baseado na implementação existente:

```java
@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {
    
    @Mock
    private PermissionRepository permissionRepository;
    
    @InjectMocks
    private PermissionService permissionService;
    
    @Test
    public void shouldReturnAllPermissions_whenFindAllIsCalled() {
        // GIVEN
        List<Permission> permissions = List.of(
            TestUtils.createPermission(1L, "create", "Pode criar algo"),
            TestUtils.createPermission(2L, "delete", "Pode deletar algo")
        );
        Mockito.when(permissionRepository.findAll()).thenReturn(permissions);
        
        // WHEN
        List<Permission> result = permissionService.findAll();
        
        // THEN
        assertThat(result)
            .hasSize(2)
            .containsExactlyInAnyOrder(permissions.get(0), permissions.get(1));
        Mockito.verify(permissionRepository, Mockito.times(1)).findAll();
    }
}
```

### TestUtils - Classe Utilitária

O projeto inclui uma classe `TestUtils` para criação padronizada de objetos de teste:

```java
// Criar entidades para teste
Permission permission = TestUtils.createPermission(1L, "create", "Descrição");
User user = TestUtils.createUser(1L, "username", "João", "Silva", "email@test.com", role);
Topic topic = TestUtils.createTopic(1L, "Frontend", "#FF5733", "Desenvolvimento frontend");
```

### Executando Testes Específicos

```bash
# Executar apenas testes de serviços de usuário
mvn test -Dtest="com.m42hub.m42hub_api.services.user.*"

# Executar apenas testes de serviços de projeto
mvn test -Dtest="com.m42hub.m42hub_api.services.project.*"

# Executar teste específico com logs detalhados
mvn test -Dtest=PermissionServiceTest -Dlogging.level.com.m42hub=DEBUG
```

## 🔧 Desenvolvimento

### Configuração do Ambiente de Desenvolvimento

1. **Configure seu IDE**
   - Instale plugins do Lombok
   - Configure formatação de código Java
   - Configure auto-import para organizar imports

2. **Banco de Desenvolvimento Local**
   ```bash
   # Criar banco PostgreSQL local
   docker run -d \
     --name postgres-dev \
     -e POSTGRES_DB=m42hub \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     postgres:15
   ```

3. **Configuração de Variáveis**
   ```bash
   # Copiar arquivo de exemplo
   cp example.env .env
   
   # Editar configurações locais
   DATABASE_URL=jdbc:postgresql://localhost:5432/m42hub
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=postgres
   SECRET=your_secret_key_here
   ```

### Migrações de Banco
```bash
# Executar migrações
mvn flyway:migrate

# Verificar status das migrações
mvn flyway:info

# Validar migrações
mvn flyway:validate

# Limpar banco (cuidado!)
mvn flyway:clean
```

### Debugging e Logs

```bash
# Executar em modo debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Executar com logs específicos
mvn spring-boot:run -Dlogging.level.com.m42hub=DEBUG

# Executar com perfil específico
mvn spring-boot:run -Dspring.profiles.active=dev
```

## 🌍 Comunidade e Suporte

- 🎥 **YouTube**: Acompanhe as [live coding sessions](https://www.youtube.com/@betgrave) onde desenvolvemos o projeto juntos!
- 💬 **Discord**: [Junte-se à nossa comunidade](https://discord.gg/E5xy62Eus2)
- 🐛 **Issues**: [Reporte bugs ou sugira features](https://github.com/m42hub/m42hub-api/issues)
- 📧 **Email**: Para questões sensíveis de segurança

## 📊 Status do Projeto

🚧 **Open Beta** - A API está em desenvolvimento ativo. Novos endpoints podem ser adicionados e mudanças podem ocorrer. Versionamento semântico será seguido para mudanças breaking.

<p align="center">
  Desenvolvido com ❤️ pela comunidade M42Hub
</p>

<p align="center">
  <a href="https://github.com/m42hub">
    <img src="https://img.shields.io/badge/GitHub-m42hub-black?style=flat&logo=github" alt="GitHub">
  </a>
  <a href="https://www.youtube.com/@betgrave">
    <img src="https://img.shields.io/badge/YouTube-BeTGrave-CC1000?style=flat&logo=youtube&logoColor=white" alt="YouTube - Betgrave">
  </a>
</p>

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

### Autenticação
- `POST /api/v1/auth/register` - Registro de usuário
- `POST /api/v1/auth/login` - Login
- `GET /api/v1/auth/validate` - Validação de token
- `POST /api/v1/auth/logout` - Logout

### Projetos
- `GET /api/v1/project` - Listar projetos (com filtros e paginação)
- `POST /api/v1/project` - Criar projeto
- `GET /api/v1/project/{id}` - Detalhes do projeto
- `PATCH /api/v1/project/{id}` - Atualizar projeto

### Usuários
- `GET /api/v1/user/me` - Dados do usuário logado
- `GET /api/v1/user` - Listar usuários (admin)
- `POST /api/v1/user` - Criar usuário (admin)

### Membros de Projeto
- `POST /api/v1/project/member/apply` - Aplicar para projeto
- `PATCH /api/v1/project/member/approve/{id}` - Aprovar aplicação
- `PATCH /api/v1/project/member/reject/{id}` - Rejeitar aplicação

### Recursos Auxiliares
- `GET /api/v1/project/topic` - Tópicos disponíveis
- `GET /api/v1/project/tool` - Ferramentas disponíveis
- `GET /api/v1/project/complexity` - Níveis de complexidade
- `GET /api/v1/project/status` - Status de projetos

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

Adoramos contribuições da comunidade! Veja como você pode ajudar:

### 1. 🐛 Reportar Bugs
- Use o [sistema de issues](https://github.com/m42hub/m42hub-api/issues)
- Descreva o problema detalhadamente
- Inclua logs e steps para reproduzir o bug

### 2. 💡 Sugerir Features
- Abra uma issue com a tag `feature request`
- Explique o valor da funcionalidade para a API
- Forneça exemplos de uso se possível

### 3. 🔧 Contribuir com Código

1. **Fork o projeto**
2. **Crie uma branch para sua feature**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Faça suas alterações**
4. **Execute os testes**
   ```bash
   mvn test
   ```
5. **Commit suas mudanças**
   ```bash
   git commit -m 'Add: amazing feature'
   ```
6. **Push para a branch**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Abra um Pull Request**

### 📋 Padrões de Código

- Use **Java 21** com as features mais recentes
- Siga as convenções do **Spring Boot**
- Mantenha **services focados** em responsabilidade única
- Escreva **testes** para novas funcionalidades
- Use **commits semânticos** (feat:, fix:, docs:, etc.)
- **Documente** endpoints complexos

### 🎥 Processo de Revisão

- **Todos os PRs são revisados ao vivo** no canal [BetGrave](https://www.youtube.com/@betgrave)! 📺
- Acompanhe as **live coding sessions** para ver seu código sendo analisado
- Em caso de conflitos durante a revisão, o autor será notificado para resolver
- Se houver problemas técnicos identificados durante a live, trabalharemos juntos para solucioná-los
- **Participe das lives** para aprender com outros contributors e melhorar suas skills!

## 🔧 Desenvolvimento

### Migrações de Banco
```bash
# Executar migrações
mvn flyway:migrate

# Verificar status
mvn flyway:info

# Limpar banco (cuidado!)
mvn flyway:clean
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

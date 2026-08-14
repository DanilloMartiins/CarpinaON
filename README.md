# CarpinaON

API REST do portal de serviços municipais de Carpina, no estilo "gov.br" municipal.

Desenvolvido em parceria: este repositório contém o back-end (API) e o app mobile (Flutter).

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.2.5 |
| Banco de dados | PostgreSQL 16 |
| Autenticação | JWT (jjwt 0.12.5) |
| Rate limit | Bucket4j 7.6.1 |
| ORM | Spring Data JPA / Hibernate |
| Container | Docker (imagem enxuta, usuário não-root) |
| CI/CD | GitHub Actions → GHCR |

## Funcionalidades

- ✅ Cadastro e login de cidadão com JWT
- ✅ Rate limit por prefeitura (50 req/10s) e por IP (20 req/10s nas rotas públicas)
- ✅ Arquitetura multi-tenant: várias prefeituras usam a mesma API, dados separados por slug
- 🔜 Catálogo de categorias e serviços
- 🔜 Solicitações com protocolo (ANO-XXXX)
- 🔜 Notificações push e in-app
- 🔜 Módulo de turismo e eventos

## Modelo de dados

- **USUARIO** — cidadão (nome, CPF, email, senha, verificação)
- **CATEGORIA** — agrupamento dos serviços
- **SERVICO** — serviço público (1 categoria)
- **SOLICITACAO** — protocolo do cidadão (1 serviço, status, endereço)
- **ANEXO** — arquivos da solicitação
- **HISTORICO_STATUS** — trilha de mudanças de status
- **NOTIFICACAO** — avisos para o usuário
- **EVENTO_TURISMO** — eventos de turismo da cidade

Fluxo de status da solicitação:

```
RECEBIDA → EM_ANALISE → EM_ANDAMENTO → RESOLVIDA | INDEFERIDA | CANCELADA
```

## Endpoints

### Autenticação (público)

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/v1/auth/cadastrar` | Cadastra um cidadão |
| POST | `/api/v1/auth/login` | Login, retorna token JWT + dados do usuário |

### Rate limit por tenant

Rotas com slug da prefeitura usam o limite por tenant:

```
/api/v1/{slug-prefeitura}/servicos
```

Resposta `429` quando estoura o limite, com mensagem amigável.

## Como rodar

### Pré-requisitos

- Java 21
- Docker (ou PostgreSQL rodando na porta 5432)

### Local (dev)

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Com Docker Compose (dev completo)

```bash
docker compose -f oracle-cloud/docker-compose.yml up -d
```

Sobe app + PostgreSQL juntos. Configure as variáveis em `oracle-cloud/.env` (copie o `.env.example`).

### Variáveis de ambiente

| Variável | Default (dev) | Descrição |
|----------|---------------|-----------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/carpinaon` | URL do banco |
| `DB_USER` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | (não commitar senha real) | Senha do banco |
| `JWT_SECRET` | (default de dev) | Chave de assinatura do JWT |
| `JWT_EXPIRATION` | `86400000` | Validade do token em ms |
| `JPA_DDL_AUTO` | `update` | Estratégia de schema (dev) |

## Imagem Docker

Build + push automático no GHCR a cada push na `main`:

```bash
ghcr.io/danillomartiins/carpinaon:latest
```

## Estrutura

```
src/main/java/com/carpinaon/
├── config/          # Security, JWT, rate limit
├── controller/      # Endpoints REST
├── dto/             # Objetos de request/response
├── exception/       # Tratamento global de erros
├── model/           # Entidades JPA + enums
├── repository/      # Acesso a dados
└── service/         # Regras de negócio
```

## Deploy (Oracle Cloud Free Tier)

Planejado para 2 VMs ARM Always Free:

- **VM1 (App):** Spring Boot + Valkey cache
- **VM2 (DB):** PostgreSQL 16 com PgBouncer (transaction mode, porta 6432)

Arquivos de provisionamento em `oracle-cloud/`.

## Licença

Em definição.

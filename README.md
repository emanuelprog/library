# 📚 Library

Sistema completo para gerenciamento de Gêneros, Autores e Livros, composto por **API REST** (backend) e **aplicação web** (frontend), com autenticação JWT, documentação Swagger e execução simplificada via **Docker Compose**.

---

## 📦 Módulos do Projeto

### 🔹 Backend (`library-backend`)
API REST desenvolvida em **Java 21 + Spring Boot**, com integração ao PostgreSQL, segurança via JWT e versionamento de endpoints (`/v1/...`).

**Funcionalidades:**
- CRUD de Gêneros, Autores e Livros
- Autenticação e autorização com JWT
- Papéis de acesso (`ROLE_READER`, `ROLE_WRITER`)
- Respostas padronizadas (`ResponseUtil`)
- Documentação Swagger
- MapStruct para conversões DTO ↔ Entidade

---

### 🔹 Frontend (`library-frontend`)
Aplicação web desenvolvida em **Vue 3 + Vite + Quasar Framework** para consumir a API do backend.

**Funcionalidades:**
- Tela de Login
- Gerenciamento de Gêneros, Autores e Livros
- Interface responsiva
- Consumo da API `/v1/...` via Axios
- Controle de permissões por papel de usuário

---

### 🔹 Banco de Dados (`library-postgres`)
Container PostgreSQL configurado via Docker, inicializado com schema e permissões definidas.

---

## 🛠️ Tecnologias Utilizadas

**Backend:**
- Java 21
- Spring Boot 3.x
- Spring Security + JWT
- PostgreSQL
- MapStruct
- Lombok
- Swagger/OpenAPI 3
- Maven

**Frontend:**
- Vue 3
- Vite
- Quasar Framework
- Axios
- TypeScript

**Infraestrutura:**
- Docker
- Docker Compose
- Nginx (servidor do frontend e proxy para o backend)

---

## 📋 Pré-requisitos

- **Java JDK 21** → [Download](https://jdk.java.net/21/)  
  Verifique: `java -version`
- **Maven 3.9+** → [Download](https://maven.apache.org/download.cgi)  
  Verifique: `mvn -version`
- **Docker** → [Guia de instalação](https://docs.docker.com/get-docker/)  
  Verifique: `docker -v`
- **Docker Compose** → [Guia de instalação](https://docs.docker.com/compose/install/)  
  Verifique: `docker compose version`

---

## ⚙️ Variáveis de Ambiente

### Backend (`library-backend/.env`)
```env
POSTGRES_DB=library_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123

APP_DB_USER=app_user
APP_DB_PASSWORD=app_user123

PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin123
```

### Frontend (`library-frontend/.env`)
```env
VITE_API_BASE_URL=/v1
```

---

## ▶️ Executando com Docker Compose

1. Clone o repositório e acesse a pasta:
   ```bash
   git clone https://github.com/seu-usuario/library.git
   cd library
   ```

2. Ajuste os arquivos `.env` do **backend** e do **frontend** conforme necessário.

3. Suba todos os containers:
   ```bash
   docker compose up -d --build
   ```

4. Acesse:
   - **Frontend:** http://localhost:9000
   - **Backend API:** http://localhost:8080/v1
   - **Swagger:** http://localhost:8080/swagger-ui/index.html
   - **PgAdmin:** http://localhost:5050

---

## 📂 Estrutura do Projeto

```
library/
├── library-backend/
│   ├── src/main/java/com/challenge/library
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── enums
│   │   ├── exception
│   │   ├── mapper
│   │   ├── repository
│   │   ├── security
│   │   ├── service
│   │   └── util
│   ├── src/main/resources
│   └── pom.xml
│
├── library-frontend/
│   ├── src/
│   │   ├── boot
│   │   ├── components
│   │   ├── css
│   │   ├── layouts
│   │   ├── pages
│   │   ├── router
│   │   ├── services
│   │   └── stores
│   │   └── tests
│   │   └── types
│   ├── public/
│   └── package.json
│
├── docker-compose.yml
└── README.md
```

---

## 🔑 Endpoints de Autenticação (Backend)

### Registro
```http
POST /v1/auth/register
Content-Type: application/json
{
  "username": "usuario",
  "password": "senha"
}
```
Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

**Regras de Atribuição de Papéis (Roles):**
- Se o `username` informado for **admin**, o usuário será criado com a role:
  ```
  ROLE_WRITER
  ```
  (permissão de leitura e escrita)
- Para qualquer outro `username`, o usuário será criado com a role:
  ```
  ROLE_READER
  ```
  (permissão apenas de leitura)


### Login
```http
POST /v1/auth/login
Content-Type: application/json
{
  "username": "usuario",
  "password": "senha"
}
```

---

## 🧑‍💻 Roles e Permissões
- `ROLE_READER`: GET em `/v1/books/**`
- `ROLE_WRITER`: GET, POST, PUT, DELETE em `/v1/books/**`
- `/v1/auth/**`: público

---

## 🧪 Testes
Para rodar os testes do backend:
```bash
mvn test
```
Para rodar os testes do frontend:
```bash
npx vitest
```

---

## 🚀 Executando em Ambiente de Desenvolvimento (sem Docker)

Se você quiser rodar **backend** e **frontend** separados, siga os passos abaixo.

---

### 🔹 Backend (`library-backend`)
**Pré-requisitos:** Java JDK 21, Maven 3.9+, PostgreSQL rodando localmente (banco `library_db` e usuário `app_user` configurados)

**Passos:**
1. Abra o IntelliJ IDEA  
2. Vá em **Run/Debug Configurations**  
3. Selecione a configuração do Spring Boot (`LibraryApplication`)  
4. Habilite o carregamento do arquivo `.env.development` (caso utilize o plugin [EnvFile](https://plugins.jetbrains.com/plugin/7861-envfile))  
5. Salve e execute 
6. Acesse no navegador: `http://localhost:8080/v1` 

## 🚀 Executando o Frontend em Ambiente de Desenvolvimento

Pré-requisitos: Node.js 18+, npm (ou yarn), Quasar CLI

1. Acesse a pasta do frontend: `cd library-frontend`  
2. Instale as dependências: `npm install`  
3. Configure a API base criando o arquivo `.env.development` com o conteúdo: `VITE_API_BASE_URL=http://localhost:8080/v1`  
4. Inicie o servidor de desenvolvimento: `npm run dev`  
5. Acesse no navegador: `http://localhost:9000`

💡 Observação: Durante o desenvolvimento, o frontend se conecta diretamente ao backend usando o valor configurado em `VITE_API_BASE_URL`.


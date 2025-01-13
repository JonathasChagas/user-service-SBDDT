# 🙍‍♂️ API de Usuário

Repositório para o exercício de um serviço para gerir os dados de usuários do curso Spring Boot Direto das Trincheiras do DevDojo. Ela oferece funcionalidades para consulta e gerenciamento de dados de usuários, incluindo id, nome, sobrenome e email

## 💾 Stack Utilizada

[![My Skills](https://skillicons.dev/icons?i=java,spring,maven)](https://skillicons.dev)

## 🐱‍🏍 Getting Started

<h3>Pré-requisitos</h3>

- [Java 21](https://github.com/)
- [Maven](https://github.com/)
- [Git 2](https://github.com)

<h3>🐱‍👤 Clonando o repositório</h3>

```bash
git clone https://github.com/JonathasChagas/user-service-SBDDT
```

<h3>🐱‍💻 Inicializando</h3>

Dentro do diretório

```bash
mvn clean install
mvn spring-boot:run
```

<h3>🧪 Executando os testes</h3>

Dentro do diretório

```bash
mvn test
```

<h2 id="routes">📍 Endpoints da API</h2>

​
| rota             | descrição                                         
|----------------------|-----------------------------------------------------
| <kbd>GET /v1/users</kbd>     | recupera as informações dos usuários [response details](#get-users-detail)
| <kbd>GET /v1/users?name="name"</kbd>     | recupera as informações pelo nome passado como parâmetro [response details](#get-users-detail)
| <kbd>GET /v1/users/{id}</kbd>     | recupera as informações pelo id passado [response details](#get-users-detail)
| <kbd>POST /v1/users</kbd>     | salva as informações de um novo usuário [request details](#post-users-detail)
| <kbd>DELETE /v1/users/{id}</kbd>     | remove as informações do usuário ligado ao id
| <kbd>PUT /v1/users</kbd>     | atualiza as informações de um usuário [request details](#put-users-detail)

<h3 id="get-users-detail">GET /v1/users</h3>

**📦 RESPONSE**
```json
{
  "name": "William",
  "second name": "Suane",
  "email": "williamsuane@email.com"
}
```
<h3 id="post-users-detail">POST /v1/users</h3>

**📝 REQUEST**
```json
{
  "name": "Jônathas",
  "second name": "Chagas",
  "email" : "jonathaschagas@email.com"
}
```

**📦 RESPONSE**
```json
{
  "id": "7", 
  "name": "Jônathas",
  "second name": "Chagas",
  "email" : "jonathaschagas@email.com"
}
```

<h3 id="put-users-detail">PUT /v1/users</h3>

**📝 REQUEST**
```json
{       
  "id": 7,
  "name": "Jônathas",
  "second name": "Chagas",
  "email" : "jonathas.dev@newemail.com"
}
```

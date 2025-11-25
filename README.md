## 🏦 README - Backend do QuitaJá (gestor de empréstimos e dívidas)

Bem-vindo ao repositório do **backend** do nosso sistema de Gestão de Empréstimos e Dívidas! Este serviço, construído em **Java** com **Spring Boot**, é o coração da aplicação, responsável pela **lógica de negócio**, o **CRUD (Create, Read, Update, Delete)** das dívidas e empréstimos, e o processamento de dados para *dashboards* e simulações.



---

## ✨ Visão Geral da API

Este backend expõe uma **API RESTful** para o frontend consumir e gerenciar todas as informações financeiras.

### 🎯 Funcionalidades e Endpoints

#### 1. Autenticação e Usuários
Gerenciamento de acesso e perfil do usuário.

| Funcionalidade | Método | Endpoint | Acesso | Descrição |
| :--- | :---: | :--- | :---: | :--- |
| **Login** | `POST` | `/api/auth/login` | 🌍 Público | Autentica o usuário e retorna o **Token JWT**. |
| **Criar Conta** | `POST` | `/api/users` | 🌍 Público | Registra um novo usuário no sistema. |
| **Dados do Usuário** | `GET` | `/api/users/me` | 🔒 Privado | Retorna os dados do usuário logado (baseado no token). |

#### 2. Gestão de Dívidas (CRUD)
O coração do sistema. Permite ao usuário gerenciar seu portfólio financeiro.

| Funcionalidade | Método | Endpoint | Acesso | Descrição |
| :--- | :---: | :--- | :---: | :--- |
| **Listar Dívidas** | `GET` | `/api/debt` | 🔒 Privado | Retorna todas as dívidas do usuário logado. |
| **Cadastrar Dívida** | `POST` | `/api/debt` | 🔒 Privado | Cria uma nova dívida vinculada a uma instituição. |
| **Atualizar Dívida** | `PUT` | `/api/debt/{id}` | 🔒 Privado | Edita valores ou detalhes de uma dívida existente. |
| **Remover Dívida** | `DELETE` | `/api/debt/{id}` | 🔒 Privado | Exclui permanentemente um registro. |

#### 3. Inteligência Financeira (Simulações)
Motor matemático que utiliza fórmulas de Juros Compostos (Tabela Price).

| Funcionalidade | Método | Endpoint | Acesso | Descrição |
| :--- | :---: | :--- | :---: | :--- |
| **Simular Renegociação** | `POST` | `/api/debt/renegotiation/simulate` | 🔒 Privado | Recebe propostas (desconto, juros, prazo) e calcula o cenário "Antes vs. Depois" com a economia real gerada. |
| **Simulador de Mercado** | `GET` | `/api/simulations` | 🔒 Privado | Compara taxas de mercado. Recebe valor/parcelas e retorna o CET de vários bancos. |

---

## 🛠️ Stack Tecnológica

| Componente | Tecnologia | Descrição |
| :--- | :--- | :--- |
| **Linguagem** | **Java** | A linguagem principal do projeto. |
| **Framework** | **Spring Boot** | Facilita a criação de aplicações *standalone* de nível de produção. |
| **Persistência** | **Spring Data JPA** | Gerenciamento ORM (Mapeamento Objeto-Relacional). |
| **Banco de Dados**| **MySQL** | O sistema de banco de dados relacional oficial do projeto. |
| **Build Tool** | **Maven** | Gerenciamento de dependências e compilação. |
| **Token de login** | **JSON Web Token (JWT)** | Gerenciamento e criação dos tokens de validação para a sessão do usuário. |

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para clonar e rodar a aplicação localmente.

### 1. Pré-requisitos

Certifique-se de ter os seguintes softwares instalados:

* **Java Development Kit (JDK) 21**
* **Spring Boot 3.5.7**
* **Maven**
* **MySQL Server**

### 2. Clonar e Configurar

```bash
# Clone o repositório
git clone https://github.com/JulianodeSouza/projeto_a3_sistemas_distribuidos_mobile_api.git
```
```bash
# Acessar pasta com o projeto
cd projeto_a3_sistemas_distribuidos_mobile_api
```
```bash
# Comando para inicializar o spring
.\mvnw.cmd spring-boot:run
```
- Lembre-se de criar o banco de dados localmente (db_quitaja)
- Alterar as variáveis no arquivo de conexão `application.properties`




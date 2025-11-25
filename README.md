## 🏦 README - Backend do QuitaJá (gestor de empréstimos e dívidas)

Bem-vindo ao repositório do **backend** do nosso sistema de Gestão de Empréstimos e Dívidas! Este serviço, construído em **Java** com **Spring Boot**, é o coração da aplicação, responsável pela **lógica de negócio**, o **CRUD (Create, Read, Update, Delete)** das dívidas e empréstimos, e o processamento de dados para *dashboards* e simulações.



---

## ✨ Visão Geral da API

Este backend expõe uma **API RESTful** para o frontend consumir e gerenciar todas as informações financeiras.

### 🎯 Funcionalidades e Endpoints-chave

| Funcionalidade | Descrição | Status/Método Exemplo |
| :--- | :--- | :--- |
| **Cadastro de usuário** | Permite **cadastrar, editar e excluir** usuários para que acessem as suas dashboards individualizadas. 
| **Lista de Dívidas (CRUD)** | Permite **cadastrar, visualizar, editar e excluir** (C.R.U.D.) dívidas e empréstimos. 
| **Dashboards** | Fornece dados agregados (totais, estatísticas) para as visualizações de *dashboards*.
| **Gerar Plano de Quitação** | Calcula e retorna um plano detalhado de pagamento para uma dívida específica.
| **Simular empréstimos** | Calcula e retorna as taxas de juros e totais a serem pagos com base em instituições financeiras.

---

## 🛠️ Stack Tecnológica

| Componente | Tecnologia | Versão Principal | Descrição |
| :--- | :--- | :--- | :--- |
| **Linguagem** | **Java** | 17+ | A linguagem principal do projeto. |
| **Framework** | **Spring Boot** | 3.x.x | Facilita a criação de aplicações *standalone* de nível de produção. |
| **Persistência** | **Spring Data JPA** | - | Gerenciamento ORM (Mapeamento Objeto-Relacional). |
| **Banco de Dados**| **MySQL** | - | O sistema de banco de dados relacional oficial do projeto. |
| **Build Tool** | **Maven** | - | Gerenciamento de dependências e compilação. |

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para clonar e rodar a aplicação localmente.

### 1. Pré-requisitos

Certifique-se de ter os seguintes softwares instalados:

* **Java Development Kit (JDK) 17+**
* **Maven**
* **MySQL Server**

### 2. Clonar e Configurar

```bash
# Clone o repositório
git clone https://github.com/JulianodeSouza/projeto_a3_sistemas_distribuidos_mobile_api.git
cd projeto_a3_sistemas_distribuidos_mobile_api
.\mvnw.cmd spring-boot:run // Comando para inicializar o spring

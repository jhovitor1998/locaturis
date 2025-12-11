# ✈️ LocaTuris - Guia de Viagens com Persistência Híbrida

LocaTuris é um sistema web completo para gestão e divulgação de pontos
turísticos.\
O projeto foi desenvolvido com foco em **Persistência Poliglota
(Híbrida)**, utilizando:

-   **PostgreSQL** para dados estruturados\
-   **MongoDB** para dados flexíveis e volumosos\
-   **Armazenamento em disco** para arquivos de mídia

------------------------------------------------------------------------

## 🚀 Funcionalidades

-   **Autenticação:** Cadastro e login de usuários (com validação de
    e-mail único)
-   **Gestão de Pontos Turísticos:** Nome, descrição, coordenadas e
    orientações de como chegar
-   **Busca:** Filtragem por cidade
-   **Avaliações e Comentários:** Notas de **0 a 5 estrelas** e
    comentários armazenados no MongoDB
-   **Galeria de Fotos:** Upload salvo em disco e metadados registrados
    no banco NoSQL
-   **Favoritos:** Usuários podem favoritar seus locais preferidos
-   **Integração com Mapas:** Geração automática de links para o Google
    Maps
-   **Hospedagens:** Registro de hotéis e pousadas vinculados aos pontos
    turísticos

------------------------------------------------------------------------

## 🛠️ Tecnologias Utilizadas

-   **Backend:** Java 17, Spring Boot 3\
-   **Frontend:** HTML5, JavaScript (Vanilla), Bootstrap 5 (SPA)\
-   **Banco Relacional (SQL):** PostgreSQL
    -   Tabelas: `usuarios`, `pontos`, `hospedagens`, `favoritos`
-   **Banco NoSQL:** MongoDB
    -   Coleções: `comentarios`, `fotos_metadata`
-   **Armazenamento:** Sistema de Arquivos (Disco Local) para imagens

------------------------------------------------------------------------

## 📋 Pré-requisitos

Antes de rodar o sistema, tenha instalado:

-   Java **JDK 17** ou superior\
-   Maven (opcional caso use uma IDE)\
-   PostgreSQL (porta padrão **5432**)\
-   MongoDB (porta padrão **27017**)

------------------------------------------------------------------------

## ⚙️ Configuração e Instalação

### 1. Clone o repositório

``` bash
git clone https://github.com/SEU-USUARIO/locaturis.git
cd locaturis
```

------------------------------------------------------------------------

### 2. Configure o Banco de Dados

Crie o banco principal no PostgreSQL:

``` sql
CREATE DATABASE locaturis_db;
```

O MongoDB criará automaticamente o banco **locaturis_mongo** na primeira
execução.

------------------------------------------------------------------------

### 3. Ajuste o arquivo `application.properties`

Local: `src/main/resources/application.properties`

``` properties
# --- PostgreSQL ---
spring.datasource.url=jdbc:postgresql://localhost:5432/locaturis_db
spring.datasource.username=seu_usuario_postgres
spring.datasource.password=sua_senha_postgres

# --- MongoDB ---
spring.data.mongodb.uri=mongodb://localhost:27017/locaturis_mongo

# --- Uploads ---
app.upload.dir=C:/locaturis_uploads/
```

> Certifique-se de que a pasta definida existe ou que o sistema tem
> permissão para criá-la.

------------------------------------------------------------------------

## ▶️ Como Rodar o Projeto

### Via IDE (IntelliJ, Eclipse, VS Code)

1.  Abra o projeto\
2.  Aguarde o Maven baixar dependências\
3.  Localize a classe principal:\
    `src/main/java/com/example/locaturis/LocaturisApplication.java`\
4.  Execute (Run)

### Via Terminal

``` bash
mvn spring-boot:run
```

A aplicação estará disponível quando aparecer a mensagem:

    Started LocaturisApplication

------------------------------------------------------------------------

## 🖥️ Como Usar

1.  Acesse no navegador:\
    **http://localhost:8080**
2.  Crie uma conta no sistema\
3.  Faça login\
4.  Cadastre um ponto turístico\
5.  Envie fotos, adicione comentários e avaliações\
6.  Explore os relacionamentos entre SQL + NoSQL + arquivos

------------------------------------------------------------------------

## 🗂️ Estrutura de Dados Híbrida

  ------------------------------------------------------------------------
  Tipo de Dado                Armazenamento           Justificativa
  --------------------------- ----------------------- --------------------
  Usuários / Login            PostgreSQL              Segurança,
                                                      consistência e
                                                      integridade
                                                      referencial

  Pontos Turísticos           PostgreSQL              Estrutura rígida e
                                                      relacionamentos
                                                      definidos

  Hospedagens                 PostgreSQL              Dados tabulares
                                                      (preço, contato,
                                                      etc.)

  Comentários                 MongoDB                 Texto livre, grande
                                                      volume, leitura
                                                      rápida

  Metadados de Fotos          MongoDB                 Flexível, permite
                                                      evoluir com EXIF e
                                                      outros dados

  Arquivos de Imagem          Disco Local             Performance e
                                                      economia (evita
                                                      inchar o banco)
  ------------------------------------------------------------------------

------------------------------------------------------------------------



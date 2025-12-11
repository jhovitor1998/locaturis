# ✈️ LocaTuris - Guia de Viagens com Persistência Híbrida

**LocaTuris** é um sistema web completo para gestão e divulgação de pontos turísticos. O projeto foi desenvolvido com foco em **Persistência Poliglota (Híbrida)**, utilizando um banco relacional (**PostgreSQL**) para dados estruturados e um banco NoSQL (**MongoDB**) para dados flexíveis e volumosos, além de armazenamento em disco para arquivos de mídia.

## 🚀 Funcionalidades

* [cite_start]**Autenticação:** Cadastro e Login de usuários (com validação de e-mail único)[cite: 12].
* [cite_start]**Gestão de Pontos Turísticos:** Cadastro completo (Nome, Descrição, Geolocalização, Como Chegar)[cite: 5].
* [cite_start]**Busca:** Filtragem de locais por cidade[cite: 8].
* [cite_start]**Avaliações e Comentários:** Sistema de notas (1-5 estrelas) e comentários armazenados em NoSQL[cite: 6, 16, 31].
* [cite_start]**Galeria de Fotos:** Upload de imagens salvo em disco local (filesystem) com metadados indexados no MongoDB[cite: 5, 15, 31].
* [cite_start]**Favoritos:** Usuários podem favoritar seus locais preferidos[cite: 102].
* [cite_start]**Integração com Mapas:** Geração dinâmica de links para o Google Maps baseados na Latitude/Longitude[cite: 18].
* [cite_start]**Hospedagens:** Cadastro de hotéis e pousadas vinculados ao ponto turístico[cite: 7, 17].

## 🛠️ Tecnologias Utilizadas

* [cite_start]**Backend:** Java 17, Spring Boot 3[cite: 26, 28].
* **Frontend:** HTML5, JavaScript (Vanilla), Bootstrap 5 (Single Page Application).
* [cite_start]**Banco Relacional (SQL):** PostgreSQL (Tabelas: Usuários, Pontos, Hospedagens, Favoritos)[cite: 23, 30].
* [cite_start]**Banco NoSQL:** MongoDB (Coleções: Comentários, Metadados de Fotos)[cite: 23, 31].
* [cite_start]**Armazenamento:** FileSystem (Disco Local) para imagens[cite: 31].




# 🐾 PetManager - Sistema de Gestão para Petshops (PDV)

![Status](https://img.shields.io/badge/status-concluído-brightgreen)
![Java Version](https://img.shields.io/badge/Java-17-blue)
![JavaFX Version](https://img.shields.io/badge/JavaFX-17-orange)
![Database](https://img.shields.io/badge/Database-MySQL-blue)

PetManager é um software de desktop completo para a gestão de petshops, desenvolvido como um projeto acadêmico. O sistema foca em funcionalidades de Ponto de Venda (PDV), permitindo o registro de vendas de produtos e serviços, controle de estoque, e gerenciamento de clientes e seus pets.

---

## 🚀 Funcionalidades Principais

- **Ponto de Venda (PDV):** Interface rápida para buscar produtos/serviços e registrar vendas.
- **Gestão de Clientes e Pets:** Cadastro, edição e visualização de clientes e seus animais associados.
- **Controle de Estoque:** Cadastro e gerenciamento de produtos, com baixa automática de estoque no momento da venda.
- **Gestão de Serviços:** Cadastro e gerenciamento dos serviços oferecidos pelo petshop.
- **Dashboard:** Painel inicial com métricas rápidas sobre o negócio.
- **Relatórios e Exportação:** Visualização do histórico de vendas com funcionalidade para exportar os dados para planilhas Excel (.xlsx).
- **Sistema de Autenticação:** Login seguro para funcionários, com senhas criptografadas.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Interface Gráfica:** JavaFX
- **Banco de Dados:** MySQL
- **Build e Dependências:** Apache Maven
- **Biblioteca Externa:** Apache POI (para manipulação de arquivos Excel)

---

## ⚙️ Como Executar o Projeto

**Pré-requisitos:**
- JDK 17 (ou superior)
- Apache Maven
- Servidor MySQL

**Passos:**
1.  Clone este repositório para a sua máquina local.
2.  Crie um banco de dados no seu MySQL e execute o script SQL completo fornecido abaixo.
3.  No arquivo `src/main/java/com/petmanager/util/ConexaoMySQL.java`, atualize o nome do banco, usuário e senha do seu MySQL.
4.  Abra um terminal na pasta raiz do projeto e execute o comando Maven para iniciar a aplicação:
    ```bash
    mvn javafx:run
    ```

---

## 📜 Script Completo do Banco de Dados


```sql
CREATE DATABASE IF NOT EXISTS petmanager_pdv;
USE petmanager_pdv;

DROP TABLE IF EXISTS venda_itens;
DROP TABLE IF EXISTS vendas;
DROP TABLE IF EXISTS servicos;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS funcionarios;

CREATE TABLE funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo ENUM('ADMINISTRADOR', 'FUNCIONARIO') NOT NULL
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(255) UNIQUE,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE pets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    especie VARCHAR(50),
    raca VARCHAR(100),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(255) NOT NULL UNIQUE,
    descricao TEXT,
    preco_venda DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE servicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_servico VARCHAR(255) NOT NULL UNIQUE,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE vendas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    id_funcionario INT NOT NULL,
    data_venda DATETIME DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    metodo_pagamento VARCHAR(50),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id),
    FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id)
);

CREATE TABLE venda_itens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_venda INT NOT NULL,
    id_produto INT,
    id_servico INT,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES vendas(id),
    FOREIGN KEY (id_produto) REFERENCES produtos(id),
    FOREIGN KEY (id_servico) REFERENCES servicos(id)
);

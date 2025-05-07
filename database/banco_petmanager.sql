CREATE DATABASE PetManagerDB;
USE PetManagerDB;

CREATE TABLE pets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100),
    especie VARCHAR(50),
    dono VARCHAR(100),
    telefone VARCHAR(20)
);
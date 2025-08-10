package com.petmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/petmanager_pdv";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("ERRO DE CONEXÃO COM O BANCO DE DADOS:");
            e.printStackTrace();
            throw new RuntimeException("Não foi possível conectar ao banco de dados.", e);
        }
    }
}
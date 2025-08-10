package com.petmanager.dao;

import com.petmanager.util.ConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public int contarTotalClientes() {
        String sql = "SELECT COUNT(id) FROM clientes";
        return contar(sql);
    }

    public int contarTotalPets() {
        String sql = "SELECT COUNT(id) FROM pets";
        return contar(sql);
    }

    public int contarAgendamentosHoje() {
        String sql = "SELECT COUNT(id) FROM agendamentos WHERE DATE(data_agendamento) = CURDATE()";
        return contar(sql);
    }

    private int contar(String sql) {
        int total = 0;
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
package com.petmanager.dao;

import com.petmanager.model.Servico;
import com.petmanager.util.ConexaoMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    public void inserir(Servico servico) throws SQLException {
        String sql = "INSERT INTO servicos (nome_servico, descricao, preco, ativo) VALUES (?, ?, ?, TRUE)";
        try (Connection conn = ConexaoMySQL.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servico.getNomeServico());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());
            stmt.executeUpdate();
        }
    }

    public List<Servico> listarTodos() {
        String sql = "SELECT * FROM servicos WHERE ativo = TRUE ORDER BY nome_servico";
        List<Servico> servicos = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getInt("id"));
                servico.setNomeServico(rs.getString("nome_servico"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setPreco(rs.getDouble("preco"));
                servicos.add(servico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicos;
    }

    public void atualizar(Servico servico) throws SQLException {
        String sql = "UPDATE servicos SET nome_servico = ?, descricao = ?, preco = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servico.getNomeServico());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());
            stmt.setInt(4, servico.getId());
            stmt.executeUpdate();
        }
    }

    public void desativar(int id) throws SQLException {
        String sql = "UPDATE servicos SET ativo = FALSE WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
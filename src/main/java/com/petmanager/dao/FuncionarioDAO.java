package com.petmanager.dao;

import com.petmanager.model.Funcionario;
import com.petmanager.util.ConexaoMySQL;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDAO {

    public Funcionario buscarPorEmail(String email) {
        String sql = "SELECT * FROM funcionarios WHERE email = ?";
        Funcionario funcionario = null;

        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcionario = new Funcionario();
                funcionario.setId(rs.getInt("id"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setEmail(rs.getString("email"));
                funcionario.setSenha(rs.getString("senha")); // Hash da senha do banco
                funcionario.setCargo(rs.getString("cargo"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionario;
    }

    public void cadastrar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (nome, email, senha, cargo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Criptografa a senha antes de salvar como solictado na aula
            String senhaHash = BCrypt.hashpw(funcionario.getSenha(), BCrypt.gensalt());

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getEmail());
            stmt.setString(3, senhaHash);
            stmt.setString(4, funcionario.getCargo());

            stmt.executeUpdate();
        }
    }
}
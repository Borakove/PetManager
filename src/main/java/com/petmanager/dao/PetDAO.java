package com.petmanager.dao;

import com.petmanager.model.Pet;
import com.petmanager.util.ConexaoMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public List<Pet> listarPorCliente(int idCliente) {
        String sql = "SELECT * FROM pets WHERE id_cliente = ?";
        List<Pet> pets = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setIdCliente(rs.getInt("id_cliente"));
                pet.setNome(rs.getString("nome"));
                pet.setEspecie(rs.getString("especie"));
                pet.setRaca(rs.getString("raca"));
                pets.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    public void inserir(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (id_cliente, nome, especie, raca) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pet.getIdCliente());
            stmt.setString(2, pet.getNome());
            stmt.setString(3, pet.getEspecie());
            stmt.setString(4, pet.getRaca());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET nome = ?, especie = ?, raca = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setInt(4, pet.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
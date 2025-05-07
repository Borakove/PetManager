package modelodesktop.petmanager.controller;

import modelodesktop.petmanager.model.Conexao;
import modelodesktop.petmanager.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetController {

    public static void salvarPet(Pet pet) {
        String sql = "INSERT INTO pets (nome, especie, dono, telefone) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.nome);
            stmt.setString(2, pet.especie);
            stmt.setString(3, pet.dono);
            stmt.setString(4, pet.telefone);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Pet> listarPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets";
        try (Connection conn = Conexao.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pets.add(new Pet(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("especie"),
                    rs.getString("dono"),
                    rs.getString("telefone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }
}

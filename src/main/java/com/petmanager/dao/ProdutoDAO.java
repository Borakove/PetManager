package com.petmanager.dao;

import com.petmanager.model.Produto;
import com.petmanager.util.ConexaoMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome_produto, descricao, preco_venda, quantidade_estoque, ativo) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = ConexaoMySQL.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNomeProduto());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produtos WHERE ativo = TRUE ORDER BY nome_produto";
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNomeProduto(rs.getString("nome_produto"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoVenda(rs.getDouble("preco_venda"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome_produto = ?, descricao = ?, preco_venda = ?, quantidade_estoque = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNomeProduto());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        }
    }

    public void desativar(int id) throws SQLException {
        String sql = "UPDATE produtos SET ativo = FALSE WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void darBaixaEstoque(int idProduto, int quantidade, Connection conn) throws SQLException {
        String sql = "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        }
    }
}
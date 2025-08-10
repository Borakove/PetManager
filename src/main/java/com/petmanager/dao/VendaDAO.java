package com.petmanager.dao;

import com.petmanager.model.*;
import com.petmanager.util.ConexaoMySQL;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public void salvarVenda(Venda venda, List<CarrinhoItem> itens) throws SQLException {
        String sqlVenda = "INSERT INTO vendas (id_cliente, id_funcionario, valor_total, metodo_pagamento) VALUES (?, ?, ?, ?)";
        String sqlItens = "INSERT INTO venda_itens (id_venda, id_produto, id_servico, quantidade, preco_unitario) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexaoMySQL.getConexao();
            conn.setAutoCommit(false);
            PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            if (venda.getIdCliente() == null) {
                stmtVenda.setNull(1, Types.INTEGER);
            } else {
                stmtVenda.setInt(1, venda.getIdCliente());
            }
            stmtVenda.setInt(2, venda.getIdFuncionario());
            stmtVenda.setDouble(3, venda.getValorTotal());
            stmtVenda.setString(4, venda.getMetodoPagamento());
            stmtVenda.executeUpdate();

            ResultSet rs = stmtVenda.getGeneratedKeys();
            int idVenda = 0;
            if (rs.next()) {
                idVenda = rs.getInt(1);
            } else {
                throw new SQLException("Não foi possível obter o ID da venda criada.");
            }

            PreparedStatement stmtItens = conn.prepareStatement(sqlItens);
            for (CarrinhoItem itemCarrinho : itens) {
                stmtItens.setInt(1, idVenda);
                if (itemCarrinho.getItem() instanceof Produto) {
                    Produto produto = (Produto) itemCarrinho.getItem();
                    stmtItens.setInt(2, produto.getId());
                    stmtItens.setNull(3, Types.INTEGER);
                    // 3. Dá baixa no estoque do produto
                    produtoDAO.darBaixaEstoque(produto.getId(), itemCarrinho.getQuantidade(), conn);
                } else { // é um Servico
                    Servico servico = (Servico) itemCarrinho.getItem();
                    stmtItens.setNull(2, Types.INTEGER);
                    stmtItens.setInt(3, servico.getId());
                }
                stmtItens.setInt(4, itemCarrinho.getQuantidade());
                stmtItens.setDouble(5, itemCarrinho.getPrecoUnitario());
                stmtItens.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            // Rollback
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<VendaRelatorioDTO> listarVendasParaRelatorio() {
        String sql = "SELECT v.id, v.data_venda, IFNULL(c.nome, 'Consumidor Final') AS nome_cliente, f.nome AS nome_funcionario, v.valor_total " +
                "FROM vendas v " +
                "JOIN funcionarios f ON v.id_funcionario = f.id " +
                "LEFT JOIN clientes c ON v.id_cliente = c.id " +
                "ORDER BY v.data_venda DESC";

        List<VendaRelatorioDTO> relatorio = new ArrayList<>();
        try (Connection conn = ConexaoMySQL.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                VendaRelatorioDTO dto = new VendaRelatorioDTO();
                dto.setIdVenda(rs.getInt("id"));
                dto.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                dto.setNomeCliente(rs.getString("nome_cliente"));
                dto.setNomeFuncionario(rs.getString("nome_funcionario"));
                dto.setValorTotal(rs.getDouble("valor_total"));
                relatorio.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatorio;
    }
}
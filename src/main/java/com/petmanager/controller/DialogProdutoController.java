package com.petmanager.controller;

import com.petmanager.dao.ProdutoDAO;
import com.petmanager.model.Produto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DialogProdutoController {
    @FXML private Label tituloLabel;
    @FXML private TextField nomeField;
    @FXML private TextField precoField;
    @FXML private TextField estoqueField;
    @FXML private TextArea descricaoArea;
    @FXML private Label mensagemLabel;

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private Stage dialogStage;
    private Produto produto;
    private boolean salvo = false;

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public boolean isSalvo() { return salvo; }

    public void setProduto(Produto produto) {
        this.produto = produto;
        if (produto != null) {
            tituloLabel.setText("Editar Produto");
            nomeField.setText(produto.getNomeProduto());
            NumberFormat formatoMoeda = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
            formatoMoeda.setMinimumFractionDigits(2);
            precoField.setText(formatoMoeda.format(produto.getPrecoVenda()));
            estoqueField.setText(String.valueOf(produto.getQuantidadeEstoque()));
            descricaoArea.setText(produto.getDescricao());
        }
    }

    @FXML void handleSalvarAction(ActionEvent event) {
        if (nomeField.getText().isEmpty() || precoField.getText().isEmpty() || estoqueField.getText().isEmpty()) {
            mensagemLabel.setText("Nome, Preço e Estoque são obrigatórios.");
            return;
        }

        if (this.produto == null) {
            this.produto = new Produto();
        }

        try {
            NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
            double preco = format.parse(precoField.getText()).doubleValue();

            produto.setNomeProduto(nomeField.getText());
            produto.setPrecoVenda(preco);
            produto.setQuantidadeEstoque(Integer.parseInt(estoqueField.getText()));
            produto.setDescricao(descricaoArea.getText());

            if (produto.getId() == 0) {
                produtoDAO.inserir(produto);
            } else {
                produtoDAO.atualizar(produto);
            }
            salvo = true;
            dialogStage.close();
        } catch (ParseException e) {
            mensagemLabel.setText("Formato de preço inválido. Use 15,00 ou 15.00");
        } catch (NumberFormatException e) {
            mensagemLabel.setText("Estoque deve ser um número inteiro.");
        } catch (SQLException e) {
            mensagemLabel.setText("Erro ao salvar no banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML void handleCancelarAction(ActionEvent event) { dialogStage.close(); }
}
package com.petmanager.controller;

import com.petmanager.dao.ClienteDAO;
import com.petmanager.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DialogClienteController {

    @FXML private Label tituloLabel;
    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private TextField emailField;
    @FXML private Label mensagemLabel;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private Cliente cliente; // Nulo p "novo cliente" e preenchido para "editar"
    private Stage dialogStage;
    private boolean salvo = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Método para preparar o dialog para edição (usaremos no futuro)
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            tituloLabel.setText("Editar Cliente");
            nomeField.setText(cliente.getNome());
            telefoneField.setText(cliente.getTelefone());
            emailField.setText(cliente.getEmail());
        }
    }

    public boolean isSalvo() {
        return salvo;
    }

    @FXML
    void handleSalvarAction(ActionEvent event) {
        if (nomeField.getText().isEmpty()) {
            mensagemLabel.setText("O nome do cliente é obrigatório.");
            return;
        }

        if (this.cliente == null) {
            this.cliente = new Cliente();
        }

        cliente.setNome(nomeField.getText());
        cliente.setTelefone(telefoneField.getText());
        cliente.setEmail(emailField.getText());

        try {
            if (cliente.getId() == 0) {
                clienteDAO.inserir(cliente);
            } else {
                clienteDAO.atualizar(cliente);
            }
            salvo = true;
            dialogStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao salvar no banco de dados.");
        }
    }

    @FXML
    void handleCancelarAction(ActionEvent event) {
        dialogStage.close();
    }
}
package com.petmanager.controller;

import com.petmanager.MainApp;
import com.petmanager.dao.FuncionarioDAO;
import com.petmanager.model.Funcionario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CadastroFuncionarioController implements Initializable {

    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private ComboBox<String> cargoComboBox;
    @FXML private Label mensagemLabel;

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargoComboBox.setItems(FXCollections.observableArrayList("ADMINISTRADOR", "FUNCIONARIO"));
    }

    @FXML
    void handleCadastroAction(ActionEvent event) {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String cargo = cargoComboBox.getSelectionModel().getSelectedItem();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cargo == null) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("Todos os campos são obrigatórios.");
            return;
        }

        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome(nome);
        novoFuncionario.setEmail(email);
        novoFuncionario.setSenha(senha);
        novoFuncionario.setCargo(cargo);

        try {
            funcionarioDAO.cadastrar(novoFuncionario);
            mensagemLabel.setTextFill(Color.GREEN);
            mensagemLabel.setText("Funcionário cadastrado com sucesso!");
            limparCampos();
        } catch (SQLException e) {
            mensagemLabel.setTextFill(Color.RED);
            // "Email duplicado"
            if (e.getErrorCode() == 1062) {
                mensagemLabel.setText("Este email já está em uso.");
            } else {
                mensagemLabel.setText("Erro ao conectar ao banco de dados.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleVoltarAction(ActionEvent event) {
        MainApp.showLoginScreen();
    }

    private void limparCampos() {
        nomeField.clear();
        emailField.clear();
        senhaField.clear();
        cargoComboBox.getSelectionModel().clearSelection();
    }
}
package com.petmanager.controller;

import com.petmanager.MainApp;
import com.petmanager.dao.FuncionarioDAO;
import com.petmanager.model.Funcionario;
import com.petmanager.util.SessaoFuncionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Label mensagemLabel;

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mensagemLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        Funcionario funcionario = funcionarioDAO.buscarPorEmail(email);

        if (funcionario != null && BCrypt.checkpw(senha, funcionario.getSenha())) {
            SessaoFuncionario.setFuncionarioLogado(funcionario);
            MainApp.showDashboard();
        } else {
            mensagemLabel.setText("Email ou senha inválidos.");
        }
    }

    @FXML
    void handleCadastroFuncionarioAction(ActionEvent event) {
        MainApp.showCadastroFuncionarioScreen();
    }
}
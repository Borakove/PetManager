package com.petmanager.controller;

import com.petmanager.MainApp;
import com.petmanager.util.SessaoFuncionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {

    @FXML
    private Label nomeFuncionarioLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SessaoFuncionario.getFuncionarioLogado() != null) {
            nomeFuncionarioLabel.setText(SessaoFuncionario.getFuncionarioLogado().getNome());
        }
    }

    @FXML
    void handleDashboardAction(ActionEvent event) {
        MainApp.showDashboard();
    }

    @FXML
    void handlePDVAction(ActionEvent event) {
        MainApp.showPontoDeVendaScreen();
    }

    @FXML
    void handleClientesAction(ActionEvent event) {
        MainApp.showGestaoClientesScreen();
    }

    @FXML
    void handleProdutosAction(ActionEvent event) {
        MainApp.showGestaoProdutosScreen();
    }

    @FXML
    void handleRelatoriosAction(ActionEvent event) {
        MainApp.showRelatoriosScreen();
    }

    @FXML
    void handleSairAction(ActionEvent event) {
        SessaoFuncionario.limparSessao();
        MainApp.showLoginScreen();
    }
}
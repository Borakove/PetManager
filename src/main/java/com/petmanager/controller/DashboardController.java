package com.petmanager.controller;

import com.petmanager.dao.DashboardDAO;
import com.petmanager.util.SessaoFuncionario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label bemVindoLabel;
    @FXML private Label clientesCountLabel;
    @FXML private Label petsCountLabel;
    @FXML private Label agendamentosHojeLabel;

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SessaoFuncionario.getFuncionarioLogado() != null) {
            String nome = SessaoFuncionario.getFuncionarioLogado().getNome().split(" ")[0];
            bemVindoLabel.setText("Bem-vindo(a), " + nome + "!");
        }

        clientesCountLabel.setText(String.valueOf(dashboardDAO.contarTotalClientes()));
        petsCountLabel.setText(String.valueOf(dashboardDAO.contarTotalPets()));
        agendamentosHojeLabel.setText(String.valueOf(dashboardDAO.contarAgendamentosHoje()));
    }
}
package com.petmanager.controller;

import com.petmanager.dao.ServicoDAO;
import com.petmanager.model.Servico;
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

public class DialogServicoController {

    @FXML private Label tituloLabel;
    @FXML private TextField nomeField;
    @FXML private TextField precoField;
    @FXML private TextArea descricaoArea;
    @FXML private Label mensagemLabel;

    private ServicoDAO servicoDAO = new ServicoDAO();
    private Stage dialogStage;
    private Servico servico;
    private boolean salvo = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSalvo() {
        return salvo;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        if (servico != null) {
            tituloLabel.setText("Editar Serviço");
            nomeField.setText(servico.getNomeServico());
            NumberFormat formatoMoeda = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
            formatoMoeda.setMinimumFractionDigits(2);
            precoField.setText(formatoMoeda.format(servico.getPreco()));
            descricaoArea.setText(servico.getDescricao());
        }
    }

    @FXML
    void handleSalvarAction(ActionEvent event) {
        if (nomeField.getText().isEmpty() || precoField.getText().isEmpty()) {
            mensagemLabel.setText("Nome e Preço são obrigatórios.");
            return;
        }

        if (this.servico == null) {
            this.servico = new Servico();
        }

        try {
            NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
            double preco = format.parse(precoField.getText()).doubleValue();

            servico.setNomeServico(nomeField.getText());
            servico.setPreco(preco);
            servico.setDescricao(descricaoArea.getText());

            if (servico.getId() == 0) {
                servicoDAO.inserir(servico);
            } else {
                servicoDAO.atualizar(servico);
            }
            salvo = true;
            dialogStage.close();
        } catch (ParseException e) {
            mensagemLabel.setText("Formato de preço inválido. Use 15,00 ou 15.00");
        } catch (SQLException e) {
            mensagemLabel.setText("Erro ao salvar no banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancelarAction(ActionEvent event) {
        dialogStage.close();
    }
}
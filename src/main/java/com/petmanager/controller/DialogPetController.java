package com.petmanager.controller;

import com.petmanager.dao.PetDAO;
import com.petmanager.model.Cliente;
import com.petmanager.model.Pet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DialogPetController {

    @FXML private Label tituloLabel;
    @FXML private TextField nomeField;
    @FXML private TextField especieField;
    @FXML private TextField racaField;
    @FXML private Label mensagemLabel;

    private PetDAO petDAO = new PetDAO();
    private Stage dialogStage;
    private Cliente dono;
    private Pet pet;
    private boolean salvo = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPet(Pet pet, Cliente dono) {
        this.dono = dono;
        this.pet = pet;
        if (pet != null) {
            tituloLabel.setText("Editar Pet");
            nomeField.setText(pet.getNome());
            especieField.setText(pet.getEspecie());
            racaField.setText(pet.getRaca());
        } else {
            tituloLabel.setText("Novo Pet para " + dono.getNome());
        }
    }

    public boolean isSalvo() {
        return salvo;
    }

    @FXML
    void handleSalvarAction(ActionEvent event) {
        if (nomeField.getText().isEmpty()) {
            mensagemLabel.setText("O nome do pet é obrigatório.");
            return;
        }

        if (this.pet == null) {
            this.pet = new Pet();
        }
        pet.setIdCliente(dono.getId());
        pet.setNome(nomeField.getText());
        pet.setEspecie(especieField.getText());
        pet.setRaca(racaField.getText());

        try {
            if (pet.getId() == 0) {
                petDAO.inserir(pet);
            } else {
                petDAO.atualizar(pet);
            }
            salvo = true;
            dialogStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao salvar o pet.");
        }
    }

    @FXML
    void handleCancelarAction(ActionEvent event) {
        dialogStage.close();
    }
}
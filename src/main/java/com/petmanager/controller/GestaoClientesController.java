package com.petmanager.controller;

import com.petmanager.MainApp;
import com.petmanager.dao.ClienteDAO;
import com.petmanager.dao.PetDAO;
import com.petmanager.model.Cliente;
import com.petmanager.model.Pet;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestaoClientesController implements Initializable {

    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, String> colClienteNome;
    @FXML private TableColumn<Cliente, String> colClienteTelefone;
    @FXML private TableColumn<Cliente, String> colClienteEmail;
    @FXML private TableColumn<Cliente, Void> colClienteAcoes;

    @FXML private TableView<Pet> petsTable;
    @FXML private TableColumn<Pet, String> colPetNome;
    @FXML private TableColumn<Pet, String> colPetEspecie;
    @FXML private TableColumn<Pet, String> colPetRaca;

    @FXML private Label detalhesClienteLabel;
    @FXML private Button adicionarPetButton;
    @FXML private TextField buscaClienteField;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PetDAO petDAO = new PetDAO();

    private Cliente clienteSelecionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colClienteTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colClienteEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        colPetNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPetEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colPetRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));

        adicionarBotoesDeAcaoClientes();
        adicionarPetButton.setDisable(true);

        clientesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    this.clienteSelecionado = newSelection;
                    if (newSelection != null) {
                        carregarPetsDoCliente(newSelection);
                        adicionarPetButton.setDisable(false);
                    } else {
                        petsTable.getItems().clear();
                        detalhesClienteLabel.setText("Selecione um Cliente");
                        adicionarPetButton.setDisable(true);
                    }
                }
        );

        carregarClientes();
    }

    private void carregarClientes() {
        clientesTable.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos()));
    }

    private void carregarPetsDoCliente(Cliente cliente) {
        detalhesClienteLabel.setText("Pets de " + cliente.getNome());
        petsTable.setItems(FXCollections.observableArrayList(petDAO.listarPorCliente(cliente.getId())));
    }

    @FXML
    void handleNovoClienteAction(ActionEvent event) {
        boolean salvo = abrirDialogoCliente(null);
        if (salvo) {
            carregarClientes();
        }
    }

    @FXML
    void handleNovoPetAction(ActionEvent event) {
        if (clienteSelecionado != null) {
            boolean salvo = abrirDialogoPet(null, clienteSelecionado);
            if (salvo) {
                carregarPetsDoCliente(clienteSelecionado);
            }
        }
    }

    private void adicionarBotoesDeAcaoClientes() {
        colClienteAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnRemover = new Button("Remover");
            private final HBox pane = new HBox(10, btnEditar, btnRemover);
            {
                pane.setAlignment(Pos.CENTER);
                btnEditar.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    boolean salvo = abrirDialogoCliente(cliente);
                    if (salvo) carregarClientes();
                });
                btnRemover.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    removerCliente(cliente);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void removerCliente(Cliente cliente) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Remover cliente '" + cliente.getNome() + "'?");
        alert.setContentText("Atenção: todos os pets associados a este cliente também serão removidos!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                clienteDAO.deletar(cliente.getId());
                carregarClientes();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert erroAlert = new Alert(Alert.AlertType.ERROR);
                erroAlert.setTitle("Erro");
                erroAlert.setHeaderText("Não foi possível remover o cliente.");
                erroAlert.showAndWait();
            }
        }
    }

    private boolean abrirDialogoCliente(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/petmanager/view/DialogCliente.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dados do Cliente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(page));

            DialogClienteController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCliente(cliente);

            dialogStage.showAndWait();

            return controller.isSalvo();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean abrirDialogoPet(Pet pet, Cliente dono) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/petmanager/view/DialogPet.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dados do Pet");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(page));

            DialogPetController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPet(pet, dono);

            dialogStage.showAndWait();
            return controller.isSalvo();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
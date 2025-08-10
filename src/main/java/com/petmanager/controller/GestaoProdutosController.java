package com.petmanager.controller;

import com.petmanager.MainApp;
import com.petmanager.dao.ProdutoDAO;
import com.petmanager.dao.ServicoDAO;
import com.petmanager.model.Produto;
import com.petmanager.model.Servico;
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

public class GestaoProdutosController implements Initializable {

    @FXML private TableView<Produto> produtosTable;
    @FXML private TableColumn<Produto, String> colProdutoNome;
    @FXML private TableColumn<Produto, Double> colProdutoPreco;
    @FXML private TableColumn<Produto, Integer> colProdutoEstoque;
    @FXML private TableColumn<Produto, Void> colProdutoAcoes;

    @FXML private TableView<Servico> servicosTable;
    @FXML private TableColumn<Servico, String> colServicoNome;
    @FXML private TableColumn<Servico, Double> colServicoPreco;
    @FXML private TableColumn<Servico, Void> colServicoAcoes;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colProdutoPreco.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colProdutoEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));

        colServicoNome.setCellValueFactory(new PropertyValueFactory<>("nomeServico"));
        colServicoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        adicionarBotoesDeAcaoProdutos();
        adicionarBotoesDeAcaoServicos();

        carregarProdutos();
        carregarServicos();
    }

    private void carregarProdutos() {
        produtosTable.setItems(FXCollections.observableArrayList(produtoDAO.listarTodos()));
    }

    private void carregarServicos() {
        servicosTable.setItems(FXCollections.observableArrayList(servicoDAO.listarTodos()));
    }

    @FXML
    void handleNovoProdutoAction(ActionEvent event) {
        boolean salvo = abrirDialogoProduto(null);
        if (salvo) {
            carregarProdutos();
        }
    }

    @FXML
    void handleNovoServicoAction(ActionEvent event) {
        boolean salvo = abrirDialogoServico(null);
        if (salvo) {
            carregarServicos();
        }
    }

    private void adicionarBotoesDeAcaoProdutos() {
        colProdutoAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnRemover = new Button("Remover");
            private final HBox pane = new HBox(10, btnEditar, btnRemover);
            {
                pane.setAlignment(Pos.CENTER);
                btnEditar.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    boolean salvo = abrirDialogoProduto(produto);
                    if (salvo) carregarProdutos();
                });
                btnRemover.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    removerProduto(produto);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void adicionarBotoesDeAcaoServicos() {
        colServicoAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnRemover = new Button("Remover");
            private final HBox pane = new HBox(10, btnEditar, btnRemover);
            {
                pane.setAlignment(Pos.CENTER);
                btnEditar.setOnAction(event -> {
                    Servico servico = getTableView().getItems().get(getIndex());
                    boolean salvo = abrirDialogoServico(servico);
                    if (salvo) carregarServicos();
                });
                btnRemover.setOnAction(event -> {
                    Servico servico = getTableView().getItems().get(getIndex());
                    removerServico(servico);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private boolean abrirDialogoProduto(Produto produto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/petmanager/view/DialogProduto.fxml"));
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dados do Produto");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(page));

            DialogProdutoController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduto(produto);

            dialogStage.showAndWait();
            return controller.isSalvo();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean abrirDialogoServico(Servico servico) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/petmanager/view/DialogServico.fxml"));
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dados do Serviço");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(page));

            DialogServicoController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setServico(servico);

            dialogStage.showAndWait();
            return controller.isSalvo();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void removerProduto(Produto produto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Desativar '" + produto.getNomeProduto() + "'?");
        alert.setContentText("O produto ficará indisponível para novas vendas.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                produtoDAO.desativar(produto.getId());
                carregarProdutos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removerServico(Servico servico) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Desativar '" + servico.getNomeServico() + "'?");
        alert.setContentText("O serviço ficará indisponível para novas vendas.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                servicoDAO.desativar(servico.getId());
                carregarServicos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
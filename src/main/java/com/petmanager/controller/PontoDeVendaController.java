package com.petmanager.controller;

import com.petmanager.dao.ClienteDAO;
import com.petmanager.dao.ProdutoDAO;
import com.petmanager.dao.ServicoDAO;
import com.petmanager.dao.VendaDAO;
import com.petmanager.model.*;
import com.petmanager.util.SessaoFuncionario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PontoDeVendaController implements Initializable {

    @FXML private TextField buscaField;
    @FXML private ListView<Object> listaBusca;
    @FXML private TableView<CarrinhoItem> carrinhoTable;
    @FXML private TableColumn<CarrinhoItem, String> colItem;
    @FXML private TableColumn<CarrinhoItem, Integer> colQtd;
    @FXML private TableColumn<CarrinhoItem, Double> colPrecoUnit;
    @FXML private TableColumn<CarrinhoItem, Double> colPrecoTotal;
    @FXML private TableColumn<CarrinhoItem, Void> colAcoes;
    @FXML private ComboBox<Cliente> clienteComboBox;
    @FXML private ComboBox<String> pagamentoComboBox;
    @FXML private Label totalLabel;
    @FXML private Label mensagemLabel;

    // Instâncias dos DAOs para acesso ao banco de dados
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VendaDAO vendaDAO = new VendaDAO();

    // Listas para gerenciar os itens
    private List<Object> todosItens = new ArrayList<>();
    private ObservableList<CarrinhoItem> carrinhoItems = FXCollections.observableArrayList();

    /**
     * Método executado automaticamente quando a tela é carregada.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todosItens.addAll(produtoDAO.listarTodos());
        todosItens.addAll(servicoDAO.listarTodos());
        configurarExibicaoListaBusca();
        listaBusca.getItems().setAll(todosItens);

        buscaField.textProperty().addListener((obs, oldText, newText) -> filtrarLista(newText));

        configurarTabelaCarrinho();

        listaBusca.setOnMouseClicked(event -> {
            Object itemSelecionado = listaBusca.getSelectionModel().getSelectedItem();
            if (itemSelecionado != null) {
                adicionarAoCarrinho(itemSelecionado);
                listaBusca.getSelectionModel().clearSelection();
            }
        });

        carregarClientes();
        pagamentoComboBox.setItems(FXCollections.observableArrayList("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix"));
    }

    private void carregarClientes() {
        clienteComboBox.getItems().setAll(clienteDAO.listarTodos());
        // Define como o objeto Cliente deve ser exibido como texto no ComboBox
        clienteComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(Cliente c) { return c == null ? "Consumidor Final" : c.getNome(); }
            @Override public Cliente fromString(String s) { return null; }
        });
    }

    private void configurarTabelaCarrinho() {
        carrinhoTable.setItems(carrinhoItems);
        colItem.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPrecoUnit.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colPrecoTotal.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));

        formatarColunaMoeda(colPrecoUnit);
        formatarColunaMoeda(colPrecoTotal);

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnRemover = new Button("Remover");
            {
                btnRemover.setOnAction(event -> removerDoCarrinho(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnRemover);
            }
        });
    }

    private void adicionarAoCarrinho(Object item) {
        CarrinhoItem novoItem = new CarrinhoItem(item);
        carrinhoItems.add(novoItem);
        atualizarTotal();
    }

    private void removerDoCarrinho(CarrinhoItem item) {
        carrinhoItems.remove(item);
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = carrinhoItems.stream().mapToDouble(CarrinhoItem::getPrecoTotal).sum();
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        totalLabel.setText("Total: " + formatoMoeda.format(total));
    }

    @FXML
    void handleFinalizarVendaAction(ActionEvent event) {
        if (carrinhoItems.isEmpty()) {
            mensagemLabel.setTextFill(Color.RED);
            mensagemLabel.setText("O carrinho está vazio!");
            return;
        }

        Venda novaVenda = new Venda();
        Cliente clienteSelecionado = clienteComboBox.getValue();
        if (clienteSelecionado != null) {
            novaVenda.setIdCliente(clienteSelecionado.getId());
        }
        novaVenda.setIdFuncionario(SessaoFuncionario.getFuncionarioLogado().getId());
        novaVenda.setMetodoPagamento(pagamentoComboBox.getValue());
        novaVenda.setValorTotal(carrinhoItems.stream().mapToDouble(CarrinhoItem::getPrecoTotal).sum());

        try {
            vendaDAO.salvarVenda(novaVenda, carrinhoItems);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Venda finalizada com sucesso!");
            alert.showAndWait();

            limparVenda();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Ocorreu um erro ao salvar a venda.");
            alert.setContentText("O estoque pode não ter sido atualizado. Verifique o console.");
            alert.showAndWait();
        }
    }

    private void limparVenda() {
        carrinhoItems.clear();
        clienteComboBox.getSelectionModel().clearSelection();
        pagamentoComboBox.getSelectionModel().clearSelection();
        atualizarTotal();
        mensagemLabel.setText("");
        buscaField.clear();
    }

    private void configurarExibicaoListaBusca() {
        listaBusca.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item instanceof Produto) {
                    setText(((Produto) item).getNomeProduto());
                } else if (item instanceof Servico) {
                    setText(((Servico) item).getNomeServico());
                }
            }
        });
    }

    private void filtrarLista(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            listaBusca.getItems().setAll(todosItens);
            return;
        }
        List<Object> itensFiltrados = new ArrayList<>();
        for (Object item : todosItens) {
            String nomeItem = "";
            if (item instanceof Produto) {
                nomeItem = ((Produto) item).getNomeProduto();
            } else if (item instanceof Servico) {
                nomeItem = ((Servico) item).getNomeServico();
            }
            if (nomeItem.toLowerCase().contains(filtro.toLowerCase())) {
                itensFiltrados.add(item);
            }
        }
        listaBusca.getItems().setAll(itensFiltrados);
    }

    private <T> void formatarColunaMoeda(TableColumn<T, Double> coluna) {
        coluna.setCellFactory(tc -> new TableCell<>() {
            private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                setText(empty || preco == null ? null : formatoMoeda.format(preco));
            }
        });
    }
}
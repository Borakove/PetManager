package com.petmanager.controller;

import com.petmanager.dao.VendaDAO;
import com.petmanager.model.VendaRelatorioDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class RelatoriosController implements Initializable {

    @FXML private TableView<VendaRelatorioDTO> vendasTable;
    @FXML private TableColumn<VendaRelatorioDTO, Integer> colIdVenda;
    @FXML private TableColumn<VendaRelatorioDTO, LocalDateTime> colData;
    @FXML private TableColumn<VendaRelatorioDTO, String> colCliente;
    @FXML private TableColumn<VendaRelatorioDTO, String> colFuncionario;
    @FXML private TableColumn<VendaRelatorioDTO, Double> colTotal;

    private final VendaDAO vendaDAO = new VendaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarDados();
    }

    private void configurarTabela() {
        colIdVenda.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colFuncionario.setCellValueFactory(new PropertyValueFactory<>("nomeFuncionario"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        // Formata a data para um formato mais legível
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colData.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
    }

    private void carregarDados() {
        vendasTable.setItems(FXCollections.observableArrayList(vendaDAO.listarVendasParaRelatorio()));
    }

    @FXML
    void handleExportarAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório de Vendas");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo Excel (*.xlsx)", "*.xlsx"));
        File arquivo = fileChooser.showSaveDialog(vendasTable.getScene().getWindow());

        if (arquivo != null) {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Relatório de Vendas");

                // Cria o cabeçalho
                Row headerRow = sheet.createRow(0);
                String[] cabecalhos = {"ID Venda", "Data", "Cliente", "Funcionário", "Valor Total"};
                for (int i = 0; i < cabecalhos.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(cabecalhos[i]);
                }

                List<VendaRelatorioDTO> vendas = vendasTable.getItems();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (int i = 0; i < vendas.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    VendaRelatorioDTO venda = vendas.get(i);
                    row.createCell(0).setCellValue(venda.getIdVenda());
                    row.createCell(1).setCellValue(venda.getDataVenda().format(formatter));
                    row.createCell(2).setCellValue(venda.getNomeCliente());
                    row.createCell(3).setCellValue(venda.getNomeFuncionario());
                    row.createCell(4).setCellValue(venda.getValorTotal());
                }

                try (FileOutputStream fileOut = new FileOutputStream(arquivo)) {
                    workbook.write(fileOut);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("Relatório exportado com sucesso!");
                alert.setContentText("O arquivo foi salvo em: " + arquivo.getAbsolutePath());
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro na Exportação");
                alert.setHeaderText("Não foi possível salvar o arquivo.");
                alert.setContentText("Ocorreu um erro de I/O. Verifique as permissões da pasta.");
                alert.showAndWait();
            }
        }
    }
}
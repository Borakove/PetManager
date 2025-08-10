package com.petmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("PetManager");

        try {
            Image icon = new Image(MainApp.class.getResourceAsStream("/images/logo.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone da aplicação. Verifique se 'logo.png' está em 'src/main/resources/images/'.");
            e.printStackTrace();
        }

        showLoginScreen();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void showLoginScreen() {
        try {
            Parent root = loadFXML("TelaLogin");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showCadastroFuncionarioScreen() {
        try {
            Parent root = loadFXML("TelaCadastroFuncionario");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showDashboard() {
        try {
            Parent root = loadFXML("Dashboard");
            Scene scene = new Scene(root, 1000, 650);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showPontoDeVendaScreen() {
        try {
            Parent root = loadFXML("PontoDeVenda");
            Scene scene = new Scene(root, 1200, 700);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showGestaoClientesScreen() {
        try {
            Parent root = loadFXML("GestaoClientes");
            Scene scene = new Scene(root, 1200, 700);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showGestaoProdutosScreen() {
        try {
            Parent root = loadFXML("GestaoProdutos");
            Scene scene = new Scene(root, 1200, 700);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showRelatoriosScreen() {
        try {
            Parent root = loadFXML("Relatorios");
            Scene scene = new Scene(root, 1200, 700);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxmlFileName) throws IOException {
        String pathToFxml = "/com/petmanager/view/" + fxmlFileName + ".fxml";
        URL fxmlUrl = MainApp.class.getResource(pathToFxml);
        if (fxmlUrl == null) {
            throw new IOException("Não foi possível encontrar o arquivo FXML: " + pathToFxml);
        }
        return FXMLLoader.load(fxmlUrl);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
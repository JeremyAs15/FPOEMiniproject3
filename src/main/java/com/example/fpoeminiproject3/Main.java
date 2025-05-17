package com.example.fpoeminiproject3;

import com.example.fpoeminiproject3.controller.GameController;
import com.example.fpoeminiproject3.model.Game;
import com.example.fpoeminiproject3.view.GameView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox welcomePane = new VBox(20);
            welcomePane.setAlignment(Pos.CENTER);
            welcomePane.setPadding(new Insets(20));

            Label welcomeLabel = new Label("Bienvenido a UNO");
            welcomeLabel.setFont(new Font(24));

            TextField nameField = new TextField();
            nameField.setMaxWidth(200);

            Button startBtn = new Button("Comenzar Juego");
            startBtn.setDisable(true);

            nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                startBtn.setDisable(newValue.trim().isEmpty());
            });

            nameField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER && !nameField.getText().trim().isEmpty()) {
                    startGame(primaryStage, nameField.getText().trim());
                }
            });

            Button instructionsBtn = new Button("Instrucciones");
            instructionsBtn.setOnAction(e -> showInstructions());

            startBtn.setOnAction(e -> {
                startGame(primaryStage, nameField.getText().trim());
            });

            welcomePane.getChildren().addAll(welcomeLabel, nameField, instructionsBtn, startBtn);
            Scene welcomeScene = new Scene(welcomePane, 400, 300);
            primaryStage.setScene(welcomeScene);
            primaryStage.setTitle("UNO - Bienvenido");
            primaryStage.show();
            nameField.requestFocus();
        } catch (Exception e) {
            showErrorAlert("Error al iniciar el juego");
        }
    }

    private void showInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instrucciones");
        alert.setHeaderText("Bienvenido a UNO");
        alert.setContentText("Instrucciones básicas:\n\n" +
                "1. Juega cartas del mismo color o número\n" +
                "2. Cartas especiales tienen efectos únicos\n" +
                "3. Di UNO! cuando tengas 1 carta\n" +
                "4. Cuando la maquina tenga 1 carta, di UNO para que tome una carta extra");
        alert.showAndWait();
    }

    private void startGame(Stage primaryStage, String playerName) {
        try {
            Game game = new Game(playerName);
            GameView gameView = new GameView(null, game);
            GameController controller = new GameController(game, gameView);
            gameView.controller = controller;

            Scene gameScene = new Scene(gameView, 800, 600);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("UNO - " + playerName);
            controller.initialize();
        } catch (Exception e) {
            showErrorAlert("Error al iniciar el juego");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
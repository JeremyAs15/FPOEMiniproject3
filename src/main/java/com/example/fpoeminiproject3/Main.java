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

/**
 * The main application class for the Game.
 * This class sets up the JavaFX application and manages the game flow.
 *
 * @author Juan Felipe Chapal - 2415537
 * @author Jeremy Astaiza - 2415667
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {
    /**
     * The main entry point for the JavaFX application.
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox welcomePane = new VBox(20);
            welcomePane.setAlignment(Pos.CENTER);
            welcomePane.setPadding(new Insets(20));

            Label welcomeLabel = new Label("Welcome to UNO Game");
            welcomeLabel.setFont(new Font(24));
            TextField nameField = new TextField();
            nameField.setPromptText("Enter your name");
            nameField.setStyle("-fx-prompt-text-fill: gray;");
            nameField.setMaxWidth(200);

            Button startBtn = new Button("Start Game");
            startBtn.setDisable(true);

            nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                startBtn.setDisable(newValue.trim().isEmpty());
            });
            nameField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER && !nameField.getText().trim().isEmpty()) {
                    startGame(primaryStage, nameField.getText().trim());
                }
            });

            Button instructionsBtn = new Button("Instructions");
            instructionsBtn.setOnAction(e -> showInstructions());
            startBtn.setOnAction(e -> {
                startGame(primaryStage, nameField.getText().trim());
            });

            welcomePane.getChildren().addAll(welcomeLabel, nameField, instructionsBtn, startBtn);

            Scene welcomeScene = new Scene(welcomePane, 400, 300);
            primaryStage.setScene(welcomeScene);
            primaryStage.setTitle("UNO GAME");
            primaryStage.show();
            nameField.requestFocus();
        } catch (Exception e) {
            showErrorAlert("Error when starting the game");
        }
    }

    /**
     * Displays the game instructions dialog.
     */
    private void showInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("Welcome to UNO");
        alert.setContentText("Basic instructions:\n\n" +
                "1. Play cards with the same color, number or symbol.\n" +
                "2. If you don't have cards to play, you must draw from the deck.\n" +
                "3. Press 'UNO' when you have 1 card to avoid penalty.\n" +
                "4. Press 'UNO' when the AI has 1 card to penalize it.\n" +
                "5. Special cards have unique effects (Skip, Reverse, Draw Two, Wild, Wild Draw Four).");
        alert.showAndWait();
    }

    /**
     * Initializes and starts the main game with the specified player name.
     * @param primaryStage the primary application stage
     * @param playerName the name of the human player
     */
    private void startGame(Stage primaryStage, String playerName) {
        try {
            Game game = new Game(playerName);
            GameView gameView = new GameView(null, game);
            GameController controller = new GameController(game, gameView);
            gameView.controller = controller;

            Scene gameScene = new Scene(gameView, 800, 600);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("UNO Game - " + playerName);
            controller.initialize();
        } catch (Exception e) {
            showErrorAlert("Error when starting the game.");
        }
    }

    /**
     * Displays an error alert dialog with the specified message.
     * @param message the error message to display
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The main method that launches the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
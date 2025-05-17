package com.example.fpoeminiproject3.view;

import com.example.fpoeminiproject3.controller.GameController;
import com.example.fpoeminiproject3.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameView extends BorderPane {
    public GameController controller;
    private final Game game;
    private final Timeline messageTimer;
    private volatile boolean unoButtonActive = false;

    private HBox playerHand;
    private HBox machineHand;
    private ImageView deckView;
    private ImageView discardPileView;
    private Label currentPlayerLabel;
    private Label messageLabel;
    private Button unoButton;
    private final Map<Card, ImageView> cardImageViews = new HashMap<>();

    public GameView(GameController controller, Game game) {
        this.controller = controller;
        this.game = game;
        this.messageTimer = new Timeline();
        initializeUI();
    }

    private void initializeUI() {
        setPadding(new Insets(20));
        setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        createTopPanel();
        createCenterPanel();
        createBottomPanel();
    }

    private void createTopPanel() {
        VBox topPanel = new VBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(10));

        currentPlayerLabel = new Label();
        currentPlayerLabel.setFont(new Font(20));
        currentPlayerLabel.setTextFill(Color.WHITE);

        messageLabel = new Label();
        messageLabel.setFont(new Font(16));
        messageLabel.setTextFill(Color.YELLOW);

        topPanel.getChildren().addAll(currentPlayerLabel, messageLabel);
        setTop(topPanel);
    }

    private void createCenterPanel() {
        GridPane centerPanel = new GridPane();
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setHgap(20);
        centerPanel.setVgap(20);
        centerPanel.setPadding(new Insets(20));

        machineHand = new HBox(10);
        machineHand.setAlignment(Pos.CENTER);
        updatePlayerHand(game.getMachinePlayer());

        HBox cardsPanel = new HBox(30);
        cardsPanel.setAlignment(Pos.CENTER);

        deckView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/fpoeminiproject3/images/deck_of_cards.png"))));
        deckView.setFitHeight(120);
        deckView.setFitWidth(80);
        deckView.setOnMouseClicked(e -> controller.handleDrawCard());

        discardPileView = new ImageView();
        discardPileView.setFitHeight(120);
        discardPileView.setFitWidth(80);
        updateDiscardPile(game.getTopDiscardCard());

        cardsPanel.getChildren().addAll(deckView, discardPileView);

        playerHand = new HBox(10);
        playerHand.setAlignment(Pos.CENTER);
        updatePlayerHand(game.getHumanPlayer());

        centerPanel.add(machineHand, 0, 0);
        centerPanel.add(cardsPanel, 0, 1);
        centerPanel.add(playerHand, 0, 2);

        setCenter(centerPanel);
    }

    private void createBottomPanel() {
        HBox bottomPanel = new HBox(20);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(20, 0, 0, 0));

        Button exitButton = new Button("Salir");
        exitButton.setFont(new Font(16));
        exitButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        exitButton.setOnAction(e -> Platform.exit()); // Solución directa alternativa

        unoButton = new Button("UNO!");
        unoButton.setFont(new Font(16));
        unoButton.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-font-weight: bold;");
        unoButton.setOnAction(e -> handleUnoButtonClick());
        unoButton.setVisible(false);

        bottomPanel.getChildren().addAll(exitButton, unoButton);
        setBottom(bottomPanel);
    }

    public void updatePlayerHand(Player player) {
        Platform.runLater(() -> {
            if (player == game.getHumanPlayer()) {
                playerHand.getChildren().clear();
                cardImageViews.clear();

                for (Card card : player.getCards()) {
                    ImageView cardView = createCardView(card);
                    cardImageViews.put(card, cardView);
                    playerHand.getChildren().add(cardView);
                }
            } else {
                machineHand.getChildren().clear();
                for (int i = 0; i < player.getDeckSize(); i++) {
                    ImageView cardBack = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/fpoeminiproject3/images/card_uno.png"))));
                    cardBack.setFitHeight(120);
                    cardBack.setFitWidth(80);
                    machineHand.getChildren().add(cardBack);
                }
            }
        });
    }

    private ImageView createCardView(Card card) {
        ImageView cardView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getImageCardPath()))));
        cardView.setFitHeight(120);
        cardView.setFitWidth(80);
        cardView.getStyleClass().add("card");
        cardView.setOnMouseClicked(e -> controller.handleCardSelection(card));
        return cardView;
    }

    public void updateDiscardPile(Card card) {
        Platform.runLater(() -> {
            if (card != null) {
                Image cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getImageCardPath())));
                discardPileView.setImage(cardImage);
            }
        });
    }

    public void updateCurrentPlayer(Player player) {
        Platform.runLater(() -> {
            currentPlayerLabel.setText("Turno de: " + player.getName());
        });
    }

    public void showTemporaryMessage(String message, int seconds) {
        Platform.runLater(() -> {
            messageLabel.setText(message);
            messageTimer.stop();
            messageTimer.getKeyFrames().clear();
            messageTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds), e -> messageLabel.setText("")));
            messageTimer.play();
        });
    }

    public void showUnoButtonWithTimeout(Player player, int timeoutSeconds) {
        Platform.runLater(() -> {
            unoButtonActive = true;

            if (player == game.getHumanPlayer()) {
                unoButton.setText("UNO!");
                unoButton.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                unoButton.setText("¡Penalizar Maquina!");
                unoButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
            }

            unoButton.setVisible(true);

            new Thread(() -> {
                try {
                    Thread.sleep(timeoutSeconds * 1000L);
                    Platform.runLater(() -> {
                        if (unoButtonActive) {
                            handleUnoTimeout(player);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });
    }

    private void handleUnoTimeout(Player player) {
        unoButtonActive = false;
        unoButton.setVisible(false);
        if (player == game.getHumanPlayer()) {
            controller.handleUnoTimeout();
        }
    }

    public void handleUnoButtonClick() {
        if (unoButtonActive) {
            unoButtonActive = false;
            if (game.getHumanPlayer().getDeckSize() == 1) {
                controller.handleUnoCall();
            } else if (game.getMachinePlayer().getDeckSize() == 1) {
                game.checkUnoPenalty(game.getMachinePlayer());
                hideUnoButton();
            }
        }
    }

    public void hideUnoButton() {
        Platform.runLater(() -> {
            unoButtonActive = false;
            unoButton.setVisible(false);
        });
    }

    public void showColorSelectionDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Seleccionar Color");
            alert.setHeaderText("Elige un color para la carta comodín");

            ButtonType blueButton = new ButtonType("Azul");
            ButtonType redButton = new ButtonType("Rojo");
            ButtonType greenButton = new ButtonType("Verde");
            ButtonType yellowButton = new ButtonType("Amarillo");

            alert.getButtonTypes().setAll(blueButton, redButton, greenButton, yellowButton);

            alert.showAndWait().ifPresent(buttonType -> {
                CardColor selectedColor = switch (buttonType.getText()) {
                    case "Azul" -> CardColor.BLUE;
                    case "Rojo" -> CardColor.RED;
                    case "Verde" -> CardColor.GREEN;
                    case "Amarillo" -> CardColor.YELLOW;
                    default -> null;
                };

                if (selectedColor != null) {
                    ((HumanPlayer)game.getHumanPlayer()).selectColor(selectedColor);
                    controller.handleColorSelection(selectedColor);
                }
            });
        });
    }

    public void showGameOver(Player winner) {
        Platform.runLater(() -> {
            updateDiscardPile(game.getTopDiscardCard());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Juego Terminado");
            alert.setHeaderText(null);
            alert.setContentText(winner.getName() + " ha ganado el juego!\nGracias por jugar.");
            alert.showAndWait();
        });
    }
}


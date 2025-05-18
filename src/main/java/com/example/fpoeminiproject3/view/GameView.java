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

/**
 * The main game view for the game, implementing the user interface.
 */
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

    /**
     * Constructs a new GameView with the specified controller and game model.
     * @param controller the game controller
     * @param game the game model
     */
    public GameView(GameController controller, Game game) {
        this.controller = controller;
        this.game = game;
        this.messageTimer = new Timeline();
        initializeUI();
    }

    /**
     * Initializes the main UI components and layout.
     */
    private void initializeUI() {
        setPadding(new Insets(20));
        setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        createTopPanel();
        createCenterPanel();
        createBottomPanel();
    }

    /**
     * Creates the top panel containing current player and message labels.
     */
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

    /**
     * Creates the center panel containing the machine's hand, deck/discard piles, and player's hand.
     */
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

    /**
     * Creates the bottom panel containing game controls.
     */
    private void createBottomPanel() {
        HBox bottomPanel = new HBox(20);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(20, 0, 0, 0));

        Button exitButton = new Button("Exit");
        exitButton.setFont(new Font(16));
        exitButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        exitButton.setOnAction(e -> Platform.exit());

        unoButton = new Button("UNO!");
        unoButton.setFont(new Font(16));
        unoButton.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-font-weight: bold;");
        unoButton.setOnAction(e -> handleUnoButtonClick());
        unoButton.setVisible(false);

        bottomPanel.getChildren().addAll(exitButton, unoButton);
        setBottom(bottomPanel);
    }

    /**
     * Updates the visual representation of a player's hand.
     * @param player the player whose hand to update
     */
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

    /**
     * Creates a clickable card view for the specified card.
     * @param card the card to create a view for
     * @return the created ImageView
     */
    private ImageView createCardView(Card card) {
        ImageView cardView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getImageCardPath()))));
        cardView.setFitHeight(120);
        cardView.setFitWidth(80);
        cardView.getStyleClass().add("card");
        cardView.setOnMouseClicked(e -> controller.handleCardSelection(card));
        return cardView;
    }

    /**
     * Updates the discard pile view with the specified card.
     * @param card the card to display on the discard pile
     */
    public void updateDiscardPile(Card card) {
        Platform.runLater(() -> {
            if (card != null) {
                Image cardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getImageCardPath())));
                discardPileView.setImage(cardImage);
            }
        });
    }

    /**
     * Updates the current player label.
     * @param player the player whose turn it is
     */
    public void updateCurrentPlayer(Player player) {
        Platform.runLater(() -> {
            currentPlayerLabel.setText("Turn of: " + player.getName());
        });
    }

    /**
     * Shows a temporary message that disappears after the specified time.
     * @param message the message to display
     * @param seconds how long to display the message
     */
    public void showTemporaryMessage(String message, int seconds) {
        Platform.runLater(() -> {
            messageLabel.setText(message);
            messageTimer.stop();
            messageTimer.getKeyFrames().clear();
            messageTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds), e -> messageLabel.setText("")));
            messageTimer.play();
        });
    }

    /**
     * Shows the UNO button with a timeout.
     * @param player the player who needs to call UNO
     * @param timeoutSeconds how long the button should be active
     */
    public void showUnoButtonWithTimeout(Player player, int timeoutSeconds) {
        Platform.runLater(() -> {
            unoButtonActive = true;

            if (player == game.getHumanPlayer()) {
                unoButton.setText("UNO!");
                unoButton.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                unoButton.setText("Penalize Machine!");
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

    /**
     * Handles the UNO button timeout.
     * @param player the player who failed to call UNO in time
     */
    private void handleUnoTimeout(Player player) {
        unoButtonActive = false;
        unoButton.setVisible(false);
        if (player == game.getHumanPlayer()) {
            controller.handleUnoTimeout();
        }
    }

    /**
     * Handles clicks on the UNO button.
     */
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

    /**
     * Hides the UNO button.
     */
    public void hideUnoButton() {
        Platform.runLater(() -> {
            unoButtonActive = false;
            unoButton.setVisible(false);
        });
    }

    /**
     * Shows a color selection dialog for wild cards.
     */
    public void showColorSelectionDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Choose a color");
            alert.setHeaderText("Choose a color for the wildcard");

            ButtonType blueButton = new ButtonType("Blue");
            ButtonType redButton = new ButtonType("Red");
            ButtonType greenButton = new ButtonType("Green");
            ButtonType yellowButton = new ButtonType("Yellow");

            alert.getButtonTypes().setAll(blueButton, redButton, greenButton, yellowButton);

            alert.showAndWait().ifPresent(buttonType -> {
                CardColor selectedColor = switch (buttonType.getText()) {
                    case "Blue" -> CardColor.BLUE;
                    case "Red" -> CardColor.RED;
                    case "Green" -> CardColor.GREEN;
                    case "Yellow" -> CardColor.YELLOW;
                    default -> null;
                };

                if (selectedColor != null) {
                    ((HumanPlayer)game.getHumanPlayer()).selectColor(selectedColor);
                    controller.handleColorSelection(selectedColor);
                }
            });
        });
    }

    /**
     * Shows the game over dialog when a player wins.
     * @param winner the winning player
     */
    public void showGameOver(Player winner) {
        Platform.runLater(() -> {
            updateDiscardPile(game.getTopDiscardCard());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game over");
            alert.setHeaderText(null);
            alert.setContentText(winner.getName() + " has won the game!\nThank you for playing.");
            alert.showAndWait();
        });
    }
}
package at.spengergasse.schwarzer_johann;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BlackjackApp extends Application {
    private Deck deck;
    private final HumanPlayer player = new HumanPlayer();
    private final Dealer dealer = new Dealer();
    private final Label status = new Label("Spieler: 0 | Dealer: 0");
    private final Label chipsLabel = new Label("Guthaben: 100");
    private final Label drawnCardLabel = new Label("Gezogene Karte: -");
    private final Label resultLabel = new Label("Ergebnis: -");
    private final TextField betField = new TextField("10");
    private Button hitButton;
    private Button standButton;
    private Button restartButton;
    private Button refillButton;
    private int bet;

    @Override
    public void start(Stage stage) {
        hitButton = new Button("Hit");
        standButton = new Button("Stand");
        restartButton = new Button("Noch mal spielen");
        restartButton.setVisible(false);
        refillButton = new Button("Guthaben auffüllen");
        refillButton.setOnAction(e -> {
            player.refillChips();
            updateChipsLabel();
        });

        hitButton.setOnAction(e -> {
            Card drawnCard = deck.draw();
            player.getHand().addCard(drawnCard);
            drawnCardLabel.setText("Gezogene Karte: " + drawnCard.toString());
            updateStatus();
            if (player.getHand().getTotalValue() > 21) {
                status.setText("Bust! Dealer gewinnt!");
                resultLabel.setText("Ergebnis: Du hast " + bet + " Chips verloren!");
                endGame();
            }
        });

        standButton.setOnAction(e -> {
            hitButton.setDisable(true);
            standButton.setDisable(true);
            new Thread(this::dealerTurn).start(); // Dealer-Zug in einem separaten Thread
        });

        restartButton.setOnAction(e -> resetGame());

        VBox root = new VBox(10, status, chipsLabel, drawnCardLabel, resultLabel, new Label("Einsatz:"), betField, hitButton, standButton, restartButton, refillButton);
        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("Blackjack");
        resetGame();
        stage.show();
    }

    private void dealerTurn() {
        while (dealer.getHand().getTotalValue() < 17) {
            Card drawnCard = deck.draw();
            dealer.getHand().addCard(drawnCard);

            // Aktualisiere die UI im JavaFX-Thread
            Platform.runLater(() -> {
                drawnCardLabel.setText("Dealer zieht: " + drawnCard.toString());
                updateStatus();
            });

            // Kurze Verzögerung, um die Aktion sichtbar zu machen
            try {
                Thread.sleep(1000); // 1 Sekunde Pause
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Dealer steht bei 17 oder mehr
        Platform.runLater(() -> {
            status.setText("Dealer steht bei " + dealer.getHand().getTotalValue() + ".");
            checkWinner();
        });
    }

    private void updateStatus() {
        status.setText("Spieler: " + player.getHand().getTotalValue() + " | Dealer: " + dealer.getHand().getTotalValue());
    }

    private void updateChipsLabel() {
        chipsLabel.setText("Guthaben: " + player.getChips());
    }

    private void checkWinner() {
        int playerValue = player.getHand().getTotalValue();
        int dealerValue = dealer.getHand().getTotalValue();

        if (playerValue > 21) {
            resultLabel.setText("Ergebnis: Du hast " + bet + " Chips verloren!");
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            player.addWinnings(bet * 2);
            resultLabel.setText("Ergebnis: Du hast " + bet + " Chips gewonnen!");
        } else if (dealerValue == playerValue) {
            player.addWinnings(bet); // Bei Unentschieden bekommt der Spieler den Einsatz zurück
            resultLabel.setText("Ergebnis: Unentschieden! Du bekommst deinen Einsatz zurück.");
        } else {
            resultLabel.setText("Ergebnis: Du hast " + bet + " Chips verloren!");
        }
        updateChipsLabel();
        endGame();
    }

    private void endGame() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        restartButton.setVisible(true);
    }

    private void resetGame() {
        try {
            bet = Integer.parseInt(betField.getText());
            if (player.getChips() < bet) {
                status.setText("Nicht genug Guthaben!");
                return;
            }
            player.placeBet(bet);
            deck = new Deck();
            player.resetHand();
            dealer.resetHand();
            status.setText("Spieler: 0 | Dealer: 0");
            drawnCardLabel.setText("Gezogene Karte: -");
            resultLabel.setText("Ergebnis: -");
            hitButton.setDisable(false);
            standButton.setDisable(false);
            restartButton.setVisible(false);
            updateChipsLabel();
        } catch (IllegalArgumentException e) {
            status.setText("Ungültiger Einsatz!");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
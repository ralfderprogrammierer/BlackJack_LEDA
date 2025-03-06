package at.spengergasse.schwarzer_johann;

import javafx.application.Application;
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
            player.getHand().addCard(deck.draw());
            updateStatus();
            if (player.getHand().getTotalValue() > 21) {
                status.setText("Bust! Dealer gewinnt!");
                endGame();
            }
        });

        standButton.setOnAction(e -> {
            hitButton.setDisable(true);
            standButton.setDisable(true);
            dealerTurn();
        });

        restartButton.setOnAction(e -> resetGame());

        VBox root = new VBox(10, status, chipsLabel, new Label("Einsatz:"), betField, hitButton, standButton, restartButton, refillButton);
        stage.setScene(new Scene(root, 400, 350));
        stage.setTitle("Blackjack");
        resetGame();
        stage.show();
    }

    private void dealerTurn() {
        while (dealer.getHand().getTotalValue() < 17) {
            dealer.getHand().addCard(deck.draw());
            updateStatus();
        }
        checkWinner();
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
            status.setText("Bust! Dealer gewinnt mit " + dealerValue + "!");
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            status.setText("Spieler gewinnt mit " + playerValue + "!");
            player.addWinnings(bet * 2);
        } else if (dealerValue == playerValue) {
            status.setText("Unentschieden! Beide haben " + playerValue + "!");
            // Bei Unentschieden bleibt das Guthaben gleich
        } else {
            status.setText("Dealer gewinnt mit " + dealerValue + "!");
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
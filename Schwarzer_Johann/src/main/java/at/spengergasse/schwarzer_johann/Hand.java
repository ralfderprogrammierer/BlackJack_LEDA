package at.spengergasse.schwarzer_johann;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        if (card != null) {
            cards.add(card);
        }
    }

    public int getTotalValue() {
        int total = 0;
        int aces = 0;

        // Zuerst alle Kartenwerte addieren und die Anzahl der Asse zählen
        for (Card card : cards) {
            total += card.value;
            if (card.rank.equals("A")) {
                aces++;
            }
        }

        // Wenn der Gesamtwert über 21 liegt und ein Ass vorhanden ist, wird das Ass als 1 gezählt
        while (total > 21 && aces > 0) {
            total -= 10; // Reduziere den Wert des Asses von 11 auf 1
            aces--;
        }

        return total;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
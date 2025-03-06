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
        for (Card card : cards) {
            total += card.value;
            if (card.rank.equals("A")) aces++;
        }
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
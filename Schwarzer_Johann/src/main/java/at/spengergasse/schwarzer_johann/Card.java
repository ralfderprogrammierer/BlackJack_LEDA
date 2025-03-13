package at.spengergasse.schwarzer_johann;

public class Card {
    String suit, rank;
    int value;

    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

package at.spengergasse.schwarzer_johann;

public class HumanPlayer extends Player {
    private int chips = 100;
    private static final int MAX_CHIPS = 10_000_000;

    public int getChips() {
        return chips;
    }

    public void placeBet(int amount) throws IllegalArgumentException {
        if (amount > chips || amount < 0) {
            throw new IllegalArgumentException("Ungültiger Einsatz!");
        }
        chips -= amount;
    }

    public void addWinnings(int amount) {
        chips = Math.min(chips + amount, MAX_CHIPS);
    }

    public void refillChips() {
        chips = Math.min(chips + 1000, MAX_CHIPS);
    }
}
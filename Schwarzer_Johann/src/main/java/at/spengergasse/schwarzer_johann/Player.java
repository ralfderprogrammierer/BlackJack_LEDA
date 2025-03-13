package at.spengergasse.schwarzer_johann;

public abstract class Player {
    protected Hand hand = new Hand();

    public Hand getHand() {
        return hand;
    }

    public void resetHand() {
        hand = new Hand();
    }
}
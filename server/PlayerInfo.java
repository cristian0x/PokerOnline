package server;

public class PlayerInfo {
    private String name;
    private String move;
    private int cash;
    private boolean actionState;
    private int bet;
    private Card[] hand = new Card[2];
    private String[] handNames;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public boolean getActionState() {
        return actionState;
    }

    public void setActionState(boolean actionState) {
        this.actionState = actionState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public Card[] getHand() {
        return hand;
    }

    public void setHand(Card rngCard,int i) {
        this.hand[i%2] = rngCard;
    }

    public String[] getHandNames() {
        return handNames;
    }

    public void setHandNames(String[] handNames) {
        this.handNames = handNames;
    }
}

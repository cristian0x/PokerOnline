package data;

import java.io.Serializable;

public class PlayerStatusInfo implements Serializable {
    private String playerAction;
    private int playerBet;
    private int playerMoney;
    private String name;
    private String[] hand;

    public String getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(String playerAction) {
        this.playerAction = playerAction;
    }

    public int getPlayerBet() {
        return playerBet;
    }

    public void setPlayerBet(int playerBet) {
        this.playerBet = playerBet;
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getHand() {
        return hand;
    }

    public void setHand(String[] hand) {
        this.hand = hand;
    }
}

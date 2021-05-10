package data;

import java.io.Serializable;
import java.util.ArrayList;

public class TableActualization implements Serializable {
    private int poolMoney;
    private int thisPlayerIndex;
    private ArrayList<PlayerStatusInfo> playerStatusInfos;
    private ArrayList<String> winners;

    public int getPoolMoney() {
        return poolMoney;
    }

    public void setPoolMoney(int poolMoney) {
        this.poolMoney = poolMoney;
    }

    public int getThisPlayerIndex() {
        return thisPlayerIndex;
    }

    public void setThisPlayerIndex(int thisPlayerIndex) {
        this.thisPlayerIndex = thisPlayerIndex;
    }

    public ArrayList<PlayerStatusInfo> getPlayerStatusInfos() {
        return playerStatusInfos;
    }

    public void setPlayerStatusInfos(ArrayList<PlayerStatusInfo> playerStatusInfos) {
        this.playerStatusInfos = playerStatusInfos;
    }

    public ArrayList<String> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<String> winners ) {
        this.winners = winners;
    }
}

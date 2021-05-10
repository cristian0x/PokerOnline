package data;

import java.io.Serializable;

public class Message implements Serializable {
    private String msgType;
    private String name;
    private String action;
    private int raiseCash;
    private int actualBet;
    private boolean canRaise;
    private boolean canCall;
    private boolean canCheck;
    private boolean canAllIn;
    private String[] cards;

    public Message(String str){
        this.name=str;
    }

    public Message(String str, String[] cards){
        this.name = "Message";
        this.msgType = str;
        this.cards = cards;
    }
    public Message(){
        this.name = "Message";
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getActualBet() {
        return actualBet;
    }

    public void setActualBet(int raiseCash) {
        this.actualBet = raiseCash;
    }

    public int getRaiseCash() {
        return raiseCash;
    }

    public void setRaiseCash(int raiseCash) {
        this.raiseCash = raiseCash;
    }

    public boolean isCanRaise() {
        return canRaise;
    }

    public void setCanRaise(boolean canRaise) {
        this.canRaise = canRaise;
    }

    public boolean isCanCall() {
        return canCall;
    }

    public void setCanCall(boolean canCall) {
        this.canCall = canCall;
    }

    public boolean isCanCheck() {
        return canCheck;
    }

    public void setCanCheck(boolean canCheck) {
        this.canCheck = canCheck;
    }

    public void setCanAllIn(boolean canAllIn) {
        this.canAllIn = canAllIn;
    }
}

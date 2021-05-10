package server;


import data.Message;
import data.PlayerStatusInfo;
import data.TableActualization;

import java.io.IOException;
import java.util.*;

public class Croupier {
    private Card[] deck = new Card[52];
    private Card[] croupierCards = new Card[5];
    private PlayerInfo[] playerInfos = new PlayerInfo[3];
    ArrayList<ClientHandler> clientList;
    private int pool;
    private int actualBet;
    private int startIdx;

    public Croupier(ArrayList<ClientHandler> clientList) {
        this.clientList = clientList;
        plInfCreate();
        valueToCards();
    }

    public void valueToCards() {
        int suit = 0;
        int val = 0;
        String suitName = new String();
        for(int i = 0; i < 52; i++) {
            val = (i%13)+2;
            switch (suit) {
                case 0 -> suitName = "spades";
                case 1 -> suitName = "diamonds";
                case 2 -> suitName = "hearts";
                case 3 -> suitName = "clubs";
            }
            switch (val) {
                case 2 -> deck[i] = new Card("2", 2, suitName);
                case 3 -> deck[i] = new Card("3", 3, suitName);
                case 4 -> deck[i] = new Card("4", 4, suitName);
                case 5 -> deck[i] = new Card("5", 5, suitName);
                case 6 -> deck[i] = new Card("6", 6, suitName);
                case 7 -> deck[i] = new Card("7", 7, suitName);
                case 8 -> deck[i] = new Card("8", 8, suitName);
                case 9 -> deck[i] = new Card("9", 9, suitName);
                case 10 -> deck[i] = new Card("10", 10, suitName);
                case 11 -> deck[i] = new Card("jack", 11, suitName);
                case 12 -> deck[i] = new Card("queen", 12, suitName);
                case 13 -> deck[i] = new Card("king", 13, suitName);
                case 14 -> deck[i] = new Card("ace", 14, suitName);
            }
            if(val == 14){
                suit++;
            }
        }
    }
    public void deckShuffle() {
        Random rng = new Random();
        for(int i = 0; i < deck.length; i++) {
            int randomIndexToSwap = rng.nextInt(deck.length);
            Card temp = deck[randomIndexToSwap];
            deck[randomIndexToSwap]=deck[i];
            deck[i]=temp;
        }
    }

    public void deal() {
        for(int i = 0; i < playerInfos.length*2; i++) {
            playerInfos[i%3].setHand(deck[i],i);
        }
        for(int i = 0; i < croupierCards.length; i++) {
            croupierCards[i] = new Card();
            croupierCards[i]=deck[playerInfos.length*2 + i];
        }
        for(int i = 0; i < clientList.size(); i++) {
            String[] hand = new String[2];
            for(int j = 0; j < 2; j++){
                Card card = playerInfos[i].getHand()[j];
                hand[j]= card.getFaceName()+"_of_"+card.getSuit();
            }
            playerInfos[i].setHandNames(hand);
            Message msg = new Message("deal", hand);
            clientList.get(i).send(msg);
        }
    }

    public void sendCroupierCards(int round) {
        int cardsNumber=0;
        int startNumber=0;
        switch (round) {
            case 0:
                cardsNumber=3;
                startNumber=0;
                break;
            case 1:
                cardsNumber=1;
                startNumber=3;
                break;
            case 2:
                cardsNumber=1;
                startNumber=4;
                break;
        }
        for(int i = 0; i < clientList.size(); i++) {
            String[] crCards = new String[cardsNumber];
            for(int j = 0; j < cardsNumber; j++){
                Card card = croupierCards[j+startNumber];
                crCards[j]= card.getFaceName()+"_of_"+card.getSuit();
            }
            Message msg = new Message("croupierCards", crCards);
            clientList.get(i).send(msg);
        }
    }
    public void sendActionRequest(int i) {
        Message msg = new Message();
        msg.setMsgType("move");
        msg.setActualBet(actualBet);
        if(playerInfos[i].getCash() >= actualBet && playerInfos[i].getBet() <= actualBet) {
            if(actualBet == 0){
                msg.setCanCall(false);
            }else {
                msg.setCanCall(true);
            }
            if(playerInfos[i].getCash() > actualBet) {
                msg.setCanRaise(true);
            }else {
                msg.setCanRaise(false);
            }
        }else {
            msg.setCanCall(false);
            msg.setCanRaise(false);
            msg.setCanAllIn(true);
        }

        if (playerInfos[i].getBet() == actualBet) {  //do big blinda
            msg.setCanCheck(true);
            msg.setCanCall(false);
        }else {
            msg.setCanCheck(false);
        }
        clientList.get(i).send(msg);
    }
    public void resetMoves() {
        for(int i = 0; i < 3; i++) {
            if(playerInfos[i].getMove() != (null)){
                if(!playerInfos[i].getMove().equals("fold")) {
                    playerInfos[i].setMove(null);
                }
            }
        }
    }
    public void resetMovesForNewRound() {
        for(int i = 0; i < 3; i++) {
            playerInfos[i].setMove(null);
        }
    }
    public void giveExtraMoney() {
        for(int i = 0; i < 3; i++) {
            playerInfos[i].setCash(playerInfos[i].getCash() + 10000);
        }
    }
    public Message getActionFromPlayer(int i) {
        Message msg = clientList.get(i).getNewMessage();
        playerInfos[i].setMove(msg.getAction());

        switch (msg.getAction()) {
            case "call":
                playerInfos[i].setCash(playerInfos[i].getCash() + playerInfos[i].getBet());
                pool = pool - playerInfos[i].getBet();
                playerInfos[i].setCash(playerInfos[i].getCash() - actualBet);

                playerInfos[i].setBet(actualBet);
                pool = pool + actualBet;
                break;
            case "raise":
                playerInfos[i].setCash(playerInfos[i].getCash() + playerInfos[i].getBet());
                pool = pool - playerInfos[i].getBet();
                actualBet = actualBet + msg.getRaiseCash();
                playerInfos[i].setCash(playerInfos[i].getCash() - actualBet);

                playerInfos[i].setBet(actualBet);
                pool = pool + actualBet;
                break;
            case "check":
                break;
            case "fold":
                playerInfos[i].setActionState(false);
                break;
        }
        return msg;
    }

    public void actualizeEveryTable(ArrayList<String> winners, boolean showCards){
        TableActualization tableActualization = new TableActualization();
        tableActualization.setPoolMoney(pool);
        ArrayList<PlayerStatusInfo> playerStatusInfos = new ArrayList<>();

        for(int i = 0; i < clientList.size(); i++){
            PlayerStatusInfo psi = new PlayerStatusInfo();
            psi.setPlayerAction(playerInfos[i].getMove());
            psi.setPlayerBet(playerInfos[i].getBet());
            psi.setPlayerMoney(playerInfos[i].getCash());
            psi.setName(playerInfos[i].getName());
            if(showCards && playerInfos[i].getActionState()){
                System.out.println("uzupelnilem gracza " + i);
                psi.setHand(playerInfos[i].getHandNames());
            }
            playerStatusInfos.add(psi);
        }
        tableActualization.setPlayerStatusInfos(playerStatusInfos);
        tableActualization.setWinners(winners);
        for(int i = 0; i < clientList.size(); i++){
            tableActualization.setThisPlayerIndex(i);
            clientList.get(i).send(tableActualization);
        }
    }

    public void plInfCreate() {
        for(int i = 0; i < clientList.size(); i++){
            playerInfos[i] = new PlayerInfo();
            playerInfos[i].setName(clientList.get(i).getPlayerName());
            playerInfos[i].setCash(10000);
        }
    }
    public void earlyWin() {
        int winIdx=0;
        for(int i = 0; i < 3; i++) {
            if (playerInfos[i].getActionState()) {
                winIdx=i;
            }
        }
        System.out.println("gracz o indeksie " + winIdx + " wygrywa " + pool);
        playerInfos[winIdx].setCash(playerInfos[winIdx].getCash()+pool);
        ArrayList<String> winner = new ArrayList<>();
        winner.add(playerInfos[winIdx].getName());
        actualizeEveryTable(winner, false);
        pool = 0;
    }
    public void smallBlindBigBlind(int i) {
        setPlayersBet();
        int smallBlind = 100;
        int bigBlind = 200;
        pool = smallBlind + bigBlind;
        playerInfos[i%3].setCash(playerInfos[i%3].getCash() - smallBlind);
        playerInfos[i%3].setBet(smallBlind);
        playerInfos[(i+1)%3].setCash(playerInfos[(i+1)%3].getCash() - bigBlind);
        playerInfos[(i+1)%3].setBet(bigBlind);
    }

    public void licytacja(int startBet) {
        resetMoves();
        int idx = startIdx;
        int playerIdx;
        actualBet = startBet;
        if(startBet == 0){
            setPlayersBet();
        }
        actualizeEveryTable(null, false);
        int raiseIndex = -1;
        int callNumber = 0;
        int checkNumber = 0;
        while(true){
            playerIdx = idx%3;
            if (playerInfos[playerIdx].getActionState()) {
                if(raiseIndex == playerIdx){
                    break;
                }
                sendActionRequest(playerIdx);
                Message msg = getActionFromPlayer(playerIdx);
                if (playingPlayersNumber() == 1) {
                    break;
                }
                if (msg.getAction().equals("raise")) {
                    raiseIndex = playerIdx;
                    callNumber = 0;
                }
                if (msg.getAction().equals("call")) {
                    callNumber++;
                }
                if (msg.getAction().equals("check")) {
                    checkNumber++;
                }
                if (callNumber == playingPlayersNumber() - 1 && raiseIndex != -1) {
                    break;
                }
                if (callNumber == playingPlayersNumber() - 1 && checkNumber == 1) {
                    break;
                }
                if (checkNumber == playingPlayersNumber()){
                    break;
                }
                actualizeEveryTable(null, false);
            }
            idx++;
        }if(playingPlayersNumber() == 1) {
            earlyWin();
        }
    }
    public int playingPlayersNumber() {
        int plPl = 0;
        for(int i = 0; i < 3; i++) {
            if(playerInfos[i].getActionState()) {
                plPl++;
            }
        }
        return plPl;
    }
    public void setPlayersBet() {
        for(int i = 0; i < 3; i++) {
            playerInfos[i].setBet(0);
        }
    }
    public void unfoldEveryone() {
        for(int i = 0; i < 3; i++) {
            playerInfos[i].setActionState(true);
        }
    }
    public void preflop() {
        unfoldEveryone();
        licytacja(200);
    }
    public void flop() {
        sendCroupierCards(0);
        licytacja(0);
    }
    public void turn() {
        sendCroupierCards(1);
        licytacja(0);
    }
    public void river() {
        sendCroupierCards(2);
        licytacja(0);
    }
    public void pickWinner() {
        FindWinner fw = new FindWinner(playerInfos, croupierCards);
        ArrayList<Integer>[] win = fw.pickWinner();
        ArrayList<String> winners = new ArrayList<>();
        if(win[0].get(0) != 1) {
            pool=pool/win[1].size();
        }
        for(int k = 0; k < win[1].size(); k++) {
            playerInfos[win[1].get(k)].setCash(playerInfos[win[1].get(k)].getCash() + pool);
            winners.add(playerInfos[win[1].get(k)].getName());
        }
        pool=0;

        actualizeEveryTable(winners, true);
    }
    public void game() {
        for(int i = 0; i < 10; i++) {
            if(i%5 == 0) {
               giveExtraMoney();
            }
            startIdx = i + 2;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Round: " + i);
            deckShuffle();
            System.out.println("Deck shuffled");
            deal();
            System.out.println("Hands sent");
            smallBlindBigBlind(i);
            resetMovesForNewRound();
            preflop();
            if(playingPlayersNumber() > 1) {
                flop();
                if(playingPlayersNumber() > 1) {
                    turn();
                    if(playingPlayersNumber() > 1) {
                        river();
                        if(playingPlayersNumber() > 1) {
                            pickWinner();
                        }
                    }
                }
            }
        }
    }
}
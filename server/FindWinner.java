package server;

import java.util.ArrayList;
import java.util.Arrays;

public class FindWinner {
    Card[] toCheck;
    int[][] handValue = new int[3][5];
    Card[] croupierCards;
    PlayerInfo[] playerInfos;
    public FindWinner(PlayerInfo[] pi, Card[] crpCards) {
        toCheck = new Card[7];
        croupierCards = crpCards;
        playerInfos=pi;
    }
    public ArrayList<Integer>[] pickWinner() {                      //zwraca arrayliste {{x},{y}} x-0remis,1win  ;  y-indeks wygranego/remis gracza
        ArrayList<Integer>[] result = new ArrayList[2];
        for (int j = 0; j < 2; j++) {
            result[j] = new ArrayList<>();
        }
        for (int i = 0; i < 3; i++){
            handValue[i]=handCheckValue(playerInfos[i]);            //ustawia wartosci reki kazdego z graczy
        }
        System.out.println("---------------------------------------");

        ArrayList<Integer>[] highPickVar = new ArrayList[7];
        for (int j = 0; j < 7; j++) {
            highPickVar[j]  = new ArrayList<>();
        }
        for (int i = 0; i < 3; i++) {                               //jesli gra to daj go do poczatkowej listy ktorych graczy porownywac
            if(playerInfos[i].getActionState()) {
                highPickVar[0].add(i);
            }
        }
        for (int j = 0; j < 5; j++) {
            int highInCol = 0;
            for (int i=0; i < highPickVar[j].size(); i++) {         //wybierz najwieksza wartosc w kolumnie
                int k=highPickVar[j].get(i);
                if(handValue[k][j] > highInCol) {
                    highInCol = handValue[k][j];
                }
            }
            for (int i = 0; i < highPickVar[j].size(); i++) {       //porownaj po kolei z najwieksza wartoscia w kolumnie i
                int k=highPickVar[j].get(i);
                if(handValue[k][j] == highInCol) {
                    highPickVar[j+1].add(k);
                }
            }
            if(highPickVar[j].size() == 1){
                result[0].add(1);
                result[1].add(highPickVar[j].get(0));
                System.out.println("Win Values : " + Arrays.toString(handValue[highPickVar[j+1].get(0)]));
                return result;
            }
            if(highPickVar[j].size() > 1){
                int count = 0;
                for (int i = 0; i < highPickVar[j].size(); i++) {
                    int k=highPickVar[j].get(i);
                    if(handValue[k][j] == 0) {
                        count++;
                        if(count == highPickVar[j].size()) {
                            System.out.println("REMIS");
                            result[0].add(0);
                            for (int z=0; z<highPickVar[j].size(); z++) {
                                result[1].add(highPickVar[j].get(z));
                                System.out.println("Tie Values : " + Arrays.toString(handValue[highPickVar[j].get(z)]));
                            }
                            return result;
                        }
                    }
                }
            }
        }
    return result;
    }

    public int[] handCheckValue(PlayerInfo pi) {                    //dajemy reke gracza i zwraca nam wartosc reki+krupiera
        toCheck = new Card[7];
        for(int i = 0; i < toCheck.length; i++) {
            toCheck[i] = new Card();
            if(i < 2) {
                toCheck[i] = pi.getHand()[i];
            }else {
                toCheck[i] = croupierCards[i-2];
            }
        }
        int[] val;
        int[] lose={0,0,0,0,0,0};
        Arrays.sort(toCheck);                                       //Sortowanie po wartosci

        val=isRoyalFlush(toCheck);
        if(Arrays.equals(val,lose)) {
            val=isStraightFlush(toCheck);
            if(Arrays.equals(val,lose)) {
                val=isFourOfAKind(toCheck);
                if(Arrays.equals(val,lose)) {
                    val=isFullHouse(toCheck);
                    if(Arrays.equals(val,lose)) {
                        val=isFlush(toCheck);
                        if(Arrays.equals(val,lose)) {
                            val=isStraight(toCheck);
                            if(Arrays.equals(val,lose)) {
                                val=isThreeOfAKind(toCheck);
                                if(Arrays.equals(val,lose)) {
                                    val=isTwoPair(toCheck);
                                    if(Arrays.equals(val,lose)) {
                                        val=isOnePair(toCheck);
                                        if(Arrays.equals(val,lose)) {
                                            val=isHighestCard(toCheck);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Hand Values : " + Arrays.toString(val));
        return val;
    }
    public int[] isRoyalFlush(Card[] hac) {                         //hac - 'hand and croupier', tak uzywane dalej
        int[] win = {10,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int[] check = {6,14,13,12,11,10};
        if(Arrays.equals(isStraight(hac), check)) {
            return win;
        }else{
            return lose;
        }
    }
    public int[] isStraightFlush(Card[] hac) {
        int[] win = {9,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        if(!Arrays.equals(isStraight(hac), lose) && !Arrays.equals(isFlush(hac), lose)) {
            int count=1;
            int highVal;
            for (int i = hac.length - 1; i > 0; i--) {
                if(hac[i].getFaceValue() - hac[i-1].getFaceValue() == 1 && hac[i].getSuit().equals(hac[i - 1].getSuit())){
                    highVal=hac[i].getFaceValue();
                    count++;
                    if(count == 5) {
                        win[2]=highVal+3;
                        return win;
                    }
                }else {
                    count=1;
                }
            }
            return lose;
        }else {
            return lose;
        }
    }
    public int[] isFourOfAKind(Card[] hac) {
        int[] win = {8,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int count;
        int cycle = 0;
        for (int j = 14; j >= 2; j--) {
            count = 0;
            for (int i = hac.length-1; i >= 0; i--) {
                if (hac[i].getFaceValue() == j) {
                    win[1]=j;
                    if(cycle == 0) {
                        win[2] = j;
                    }
                    cycle++;
                    count++;
                }
                if(count == 4) {
                    if(cycle == 4) {
                        win[2] = hac[i - 1].getFaceValue();
                    }
                    return win;
                }
            }
        }
        return lose;
    }
    public int[] isFullHouse(Card[] hac) {
        int[] win = {7,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int count;
        int cycle2 = 0;
        int[] possible2 = {0,0,0};
        int possible3 = 0;
        for(int i = 2; i < 15; i++) {
            count = 0;
            for(int j = 0; j < hac.length; j++) {
                if(hac[j].getFaceValue() == i) {
                    count++;
                    if(count == 2) {
                        possible2[cycle2] = i;
                        cycle2++;
                    }
                    if(count == 3) {
                        possible3 = i;
                        win[1] = i;
                    }
                }
            }
        }
        for(int i = 0; i < 3; i++) {
            if(possible2[i] != possible3 && possible2[i] > win[2]) {
                win[2] = possible2[i];
            }
        }
        if(win[1] != 0 && win[2] != 0) {
            return win;
        }else {
            return lose;
        }
    }
    public int[] isFlush(Card[] hac){
        int[] win = {6,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int[] suit = {0,0,0,0};
        String winSuit = "";
        for (int i = 0; i < hac.length; i++) {
            switch (hac[i].getSuit()) {
                case "spades" -> {
                    suit[0]++;
                    if (suit[0] == 5) {
                        winSuit = "spades";
                    }
                }
                case "diamonds" -> {
                    suit[1]++;
                    if (suit[1] == 5) {
                        winSuit = "diamonds";
                    }
                }
                case "hearts" -> {
                    suit[2]++;
                    if (suit[2] == 5) {
                        winSuit = "hearts";
                    }
                }
                case "clubs" -> {
                    suit[3]++;
                    if (suit[3] == 5) {
                        winSuit = "clubs";
                    }
                }
            }
        }
        for (int i = 0; i < suit.length; i++) {
            if(suit[i] >= 5) {
                int j = 1;
                for (int k = hac.length-1; k >= 0; k--) {
                    if (hac[k].getSuit().equals(winSuit)) {
                        win[j] = hac[k].getFaceValue();
                        j++;
                        if(j == 6) {
                            break;
                        }
                    }
                }
                return win;
            }
        }
        return lose;
    }
    public int[] isStraight(Card[] hac) {
        int[] win = {5,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int count = 1;
        int highVal;
        for (int i = hac.length-1; i > 0; i--) {
            if(hac[i].getFaceValue() - hac[i - 1].getFaceValue() == 1) {
                highVal = hac[i].getFaceValue();
                count++;
                if(count == 5) {
                    win[1] = highVal+3;
                    return win;
                }
            }else {
                count = 1;
            }
        }
        return lose;
    }
    public int[] isThreeOfAKind(Card[] hac) {
        int[] win = {4,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int count;
        int cycle = 0;
        for (int j = 14; j >= 2; j--) {
            count=0;
            for (int i = hac.length-1; i >= 0; i--) {
                if (hac[i].getFaceValue()==j) {
                    count++;
                    cycle++;
                    if(count == 3) {
                        if(cycle == 3) {
                            win[2] = hac[i - 1].getFaceValue();
                            win[3] = hac[i - 2].getFaceValue();
                        }else if(cycle == 4) {
                            win[2] = hac[hac.length-1].getFaceValue();
                            win[3] = hac[i - 1].getFaceValue();
                        }else {
                            win[2] = hac[hac.length-1].getFaceValue();
                            win[3] = hac[hac.length-2].getFaceValue();
                        }
                        win[1] = j;
                        return win;
                    }
                }
            }
        }
        return lose;
    }
    public int[] isTwoPair(Card[] hac) {
        int[] win = {3,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int count = 0;
        int cycle = 0;
        for (int i = hac.length-1; i > 0; i--) {
            if((count == 0 && cycle == 0) || (count == 1 && cycle == 1)) {
                win[3] = hac[i].getFaceValue();
            }
            int j = i - 1;
            if (hac[i].getFaceValue() == hac[j].getFaceValue()) {
                if(count == 0){
                    win[1] = hac[i].getFaceValue();
                    i = j;
                }
                if(count == 1){
                    win[2] = hac[j].getFaceValue();
                    if(cycle == 1){
                        win[3] = hac[hac.length-5].getFaceValue();
                    }
                    return win;
                }
                count++;
            }
            cycle++;
        }
        return lose;
    }
    public int[] isOnePair(Card[] hac) {
        int[] win = {2,0,0,0,0,0};
        int[] lose = {0,0,0,0,0,0};
        int cycle = 0;
        for (int i = hac.length-1; i > 0; i--) {
            int j = i-1;
            if (hac[i].getFaceValue() == hac[j].getFaceValue()) {
                win[1] = hac[i].getFaceValue();
                if(cycle == 0){
                    win[2] = hac[hac.length-3].getFaceValue();
                    win[3] = hac[hac.length-4].getFaceValue();
                    win[4] = hac[hac.length-5].getFaceValue();
                }else if(cycle == 1){
                    win[2]= hac[hac.length-1].getFaceValue();
                    win[3]= hac[hac.length-4].getFaceValue();
                    win[4]= hac[hac.length-5].getFaceValue();
                }else if(cycle == 2){
                    win[2] = hac[hac.length-1].getFaceValue();
                    win[3] = hac[hac.length-2].getFaceValue();
                    win[4] = hac[hac.length-5].getFaceValue();
                }else {
                    win[2] = hac[hac.length-1].getFaceValue();
                    win[3] = hac[hac.length-2].getFaceValue();
                    win[4] = hac[hac.length-3].getFaceValue();
                }
                return win;
            }
            cycle++;
        }
        return lose;
    }
    public int[] isHighestCard(Card[] hac) {
        int[] win = {1,0,0,0,0,0};
        for (int i = 1; i < win.length; i++) {
            win[i] = hac[hac.length-i].getFaceValue();
        }
        return win;
    }
}
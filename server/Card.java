package server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Card implements Comparable<Card>{
    private String faceName;
    private int faceValue;
    private String suit;

    /**
     *
     * @param faceName = 2,3,4...,9,10,Jack,Queen,King,Ace
     * @param faceValue = 2,3,4,...,13,14
     * @param suit "spades", "clubs", "diamonds", "hearts"
     */

    public Card() {
    }
    public Card(String faceName, int faceValue, String suit) {
        this.faceName = faceName;
        this.faceValue = faceValue;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "faceName='" + faceName + '\'' +
                ", faceVaule=" + faceValue +
                ", suit='" + suit + '\'' +
                '}';
    }

    public String getFaceName() {
        return faceName;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card card) {
        return this.faceValue - card.faceValue;
    }
}

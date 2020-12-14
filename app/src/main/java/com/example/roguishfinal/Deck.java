package com.example.roguishfinal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Deck implements Serializable {
    // Member Variables
    protected String name;
    protected Entity owner;
    protected ArrayList<Card> startingDeck = new ArrayList<>();
    protected ArrayList<Card> hand = new ArrayList<>();

    // Accessors
    public String getName() { return this.name; }
    public Entity getOwner() { return this.owner; }
    public ArrayList<Card> getHand() { return this.hand; }

    // Return a card by index
    public Card getCard(int index) {
        if(index > this.startingDeck.size() || index < 0) {
            return null;
        }

        return startingDeck.get(index);
    }

    public int getIndex(Card card) {
        if(!this.startingDeck.contains(card))
            return -1;

        return this.startingDeck.indexOf(card);
    }

    // Return a random card from the deck (ignores slot 0)
    public Card getRandomCard() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, startingDeck.size());
        return getCard(randomNum);
    }
}


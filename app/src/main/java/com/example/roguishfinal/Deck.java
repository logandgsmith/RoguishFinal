package com.example.roguishfinal;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Deck {
    // Member Variables
    protected String name;
    protected Entity owner;
    protected ArrayList<Card> startingDeck = new ArrayList<>();
    protected ArrayList<Card> hand = new ArrayList<>();

    // Accessors
    public String getName() { return this.name; }
    public Entity getOwner() { return this.owner; }

    // Return a card by index
    public Card getCard(int index) {
        if(index > this.startingDeck.size() || index < 0) {
            return null;
        }

        return startingDeck.get(index);
    }

    // Return a random card from the deck
    public Card getRandomCard() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, startingDeck.size());
        return getCard(randomNum);
    }
}


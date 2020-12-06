package com.example.roguishfinal;

import java.util.ArrayList;

public abstract class Deck {
    protected String name;
    protected int currentCard = 0;
    protected ArrayList<Card> startingDeck;

    public Card getCard(int index) {
        if(index > this.startingDeck.size() || index < 0) {
            return null;
        }

        return startingDeck.get(index);
    }

    public Card getCurrent() {
        return startingDeck.get(currentCard);
    }

    public Card getNext() {
        this.currentCard++;
        if(this.currentCard >= this.startingDeck.size())
            this.currentCard = 0;

        return startingDeck.get(currentCard);
    }
}


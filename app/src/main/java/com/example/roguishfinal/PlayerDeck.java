package com.example.roguishfinal;

import java.util.ArrayList;

public class PlayerDeck extends Deck {
    // Additional Variables
    protected int currentCard = 0;

    // Constructor
    public PlayerDeck() {
        name = "Player";
        owner = new Entity(
                this.name,
                100,
                25,
                5
        );

        // Add cards to deck
        startingDeck.add(
                new Card(
                        "Strike",
                        String.format(
                                "Attack the enemy for %s",
                                this.owner.getAttack()
                        ),
                        Target.ENEMY,
                        this.owner::attack
                )
        );
        startingDeck.add(
                new Card(
                        "Neurotoxin",
                        String.format(
                                "Apply %s poison to the enemy.",
                                this.owner.getMagic()
                        ),
                        Target.ENEMY,
                        this.owner::poison
                )
        );
        startingDeck.add(
                new Card(
                        "Flood Systems",
                        "Gain 2 Evasion (Take no damage from next attack",
                        Target.ENEMY,
                        this.owner::evade
                )
        );
        startingDeck.add(
                new Card(
                        "Vita-gel",
                        "Heal for 35 (Can overheal).",
                        Target.SELF,
                        this.owner::heal
                )
        );

        this.drawHand();
    }

    // Return the card in hand
    public Card getCurrent() {
        return this.hand.get(currentCard);
    }

    // Draw a hand of 3 cards (Duplicates are ok)
    public void drawHand() {
        if(startingDeck.size() == 0) return;

        this.hand.clear();

        for(int i = 0; i < 3; i++) {
            this.hand.add(getRandomCard());
        }

        this.currentCard = 0;
    }

    // Get the next card from the deck (reverse to get last card)
    public Card getNext(boolean reverse) {
        // Find direction to move
        this.currentCard = reverse ? this.currentCard - 1 : this.currentCard + 1;

        // Handle out of bounds
        if(this.currentCard >= this.hand.size())
            this.currentCard = 0;
        else if(this.currentCard < 0)
            this.currentCard = this.hand.size() - 1;

        return this.hand.get(this.currentCard);
    }
}

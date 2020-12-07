package com.example.roguishfinal;

import java.util.ArrayList;

public class PlayerDeck {
    // Member Variables
    private String name;
    private Entity owner;
    private int currentCard = 0;
    private ArrayList<Card> startingDeck;

    // Constructor
    public PlayerDeck() {
        name = "Player";
        owner = new Entity(
                this.name,
                100,
                25,
                5
        );
        startingDeck = new ArrayList<>();

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
    }

    // Accessors
    public String getName() { return this.name; }
    public Entity getOwner() { return this.owner; }


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

package com.example.roguishfinal;

import java.util.ArrayList;

public class EnemyDeck {

    // Member Variables
    private String name;
    private Entity owner;
    private ArrayList<Card> startingDeck;

    // Constructor
    public EnemyDeck() {
        name = "TestEnemy";
        owner = new Entity(
                this.name,
                100,
                10,
                2
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
                        this.owner::poison
                )
        );
    }

    // Accessors
    public String getName() { return this.name; }
    public Entity getOwner() { return this.owner; }
}

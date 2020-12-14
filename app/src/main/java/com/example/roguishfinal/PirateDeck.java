package com.example.roguishfinal;

public class PirateDeck extends Deck {
    // Constructor
    public PirateDeck() {
        name = "Starbuckler Captain";
        owner = new Entity(
                this.name,
                100,
                15,
                5
        );

        // Add cards to deck
        startingDeck.add(
                new Card(
                        "Pass",
                        "Do nothing and pass the turn.",
                        Target.ENEMY,
                        this.owner::attack
                )
        );
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
                        "Nanite Dagger",
                        "Attack for 10, heal 5",
                        Target.ENEMY,
                        this.owner::naniteAttack
                )
        );
        startingDeck.add(
                new Card(
                        "Nanocharge",
                        "Attack damage is multiplied by your 1 + charge when >0.",
                        Target.SELF,
                        this.owner::nanoCharge
                )
        );
    }
}

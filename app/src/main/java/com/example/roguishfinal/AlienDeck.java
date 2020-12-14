package com.example.roguishfinal;

public class AlienDeck extends Deck {
    // Constructor
    public AlienDeck() {
        name = "Xythoid Elite";
        owner = new Entity(
                this.name,
                150,
                10,
                10
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
                        "Overwhelming Force",
                        "Deal damage equal to Attack + Magic",
                        Target.ENEMY,
                        this.owner::overwhelmingForce
                )
        );
        startingDeck.add(
                new Card(
                        "Unnatural Defenses",
                        "Gain Armor equal to 2 times your magic",
                        Target.SELF,
                        this.owner::unnaturalDefenses
                )
        );
    }
}

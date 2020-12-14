package com.example.roguishfinal;

import java.io.Serializable;

// Used to make passing functions easier
interface CardFunctionality extends Serializable {
    void playCard(Entity target);
}

enum Target implements Serializable {
    SELF,
    ENEMY
}

// Represents a single Card in any given Deck
public class Card implements Serializable {
    // Member variables
    private final String name;
    private final String description;
    private final Target target;
    public final CardFunctionality playCard;

    // Constructor
    public Card(String name, String description, Target target, CardFunctionality playCard) {
        this.name = name;
        this.description = description;
        this.target = target;
        this.playCard = playCard;
    }

    // Accessors
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public Target getTarget() { return this.target; }
}

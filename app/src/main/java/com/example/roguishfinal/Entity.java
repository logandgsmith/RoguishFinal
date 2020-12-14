package com.example.roguishfinal;

import java.io.Serializable;

public class Entity implements Serializable {
    // Member Variables /////
    private final String name;
    private int health;
    private int attack;
    private int magic;

    private int poison = 0;
    private int evasion = 0;
    private int charge = 0;
    private int armor = 0;

    private boolean isFinished = false;

    ///// Constructor /////
    public Entity(String name, int health, int attack, int magic) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.magic = magic;
    }

    ///// Accessors /////
    // Stats
    public int getHealth() { return this.health; }
    public int getAttack() { return this.attack; }
    public int getMagic() { return this.magic; }

    // Effects
    public int getEvasion() { return this.evasion; }
    public int getPoison() { return this.poison; }
    public int getCharge() { return this.charge; }
    public int getArmor() { return this.armor; }

    // States
    public boolean getIsFinished() { return this.isFinished; }

    ///// Mutators /////
    public void breakArmor() {
        this.armor = 0;
    }

    // Wrapper when an attack could be evaded
    public void takeAttack(int damage) {
        if(this.evasion > 0)
            this.evasion--;
        else
            this.takeDamage(damage);
    }

    // Add to the poison counter
    public void takePoison(int poison) { this.poison += poison; }

    // Directly assign damage -- do not call if attack can be evaded
    public void takeDamage(int damage) {
        this.health -= damage;
        if(this.health < 0)
            this.health = 0;
    }

    // Actions that run on each turn
    public void upkeep() {
        // Take poison damage
        if(poison > 0) {
            this.takeDamage(poison);
            this.poison--;
        }

        // Check if the entity is finished
        if(this.health <= 0)
            isFinished = true;
    }

    // Actions
    public void attack(Entity target) {
        int weightedAttack = this.attack;
        if(this.charge > 0) {
            weightedAttack = (this.charge + 1)  * attack;
            this.charge--;
        }

        if(target.getArmor() > 0) {
            weightedAttack -= target.armor;
            target.breakArmor();
        }

        target.takeAttack(weightedAttack);
    }

    public void poison(Entity target) {
        target.takePoison(this.magic);
    }

    public void evade(Entity target) {
        this.evasion += 2;
    }

    public void heal(Entity target) {
        target.takeDamage(-30);
    }

    public void naniteAttack(Entity target) {
        target.takeAttack(this.attack);
        this.health += 5;
    }

    public void nanoCharge(Entity target) {
        this.charge += 1;
    }

    public void overwhelmingForce(Entity target) {
        target.takeAttack(this.attack + this.magic);
    }

    public void unnaturalDefenses(Entity target) {
        this.armor += this.magic;
    }
}

package com.example.roguishfinal;

public class Entity {
    // Member Variables /////
    private final String name;
    private int health;
    private int attack;
    private int magic;

    private int poison = 0;
    private int evasion = 0;
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

    // States
    public boolean getIsFinished() { return this.isFinished; }

    ///// Mutators /////
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
        target.takeAttack(this.attack);
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
}

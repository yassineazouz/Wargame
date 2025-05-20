package tn.isty.wargame.model;

import java.io.Serializable;

public class Unit implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String type;           // ex: "archer", "infanterie"
    private final int maxHealth;
    private final int attack;
    private final int defense;
    private final int maxMovement;
    private final int visionRange;

    private int currentHealth;
    private int currentMovement;

    private Player owner;                // le joueur à qui appartient l’unité
    private HexagonTile position;        // case actuelle sur le plateau

    private boolean wasAttackedThisTurn = false; // ✅ Nouveau champ

    public Unit(String name, String type, int maxHealth, int attack, int defense, int maxMovement, int visionRange, Player owner) {
        this.name = name;
        this.type = type;
        this.maxHealth = maxHealth;
        this.attack = attack;
        this.defense = defense;
        this.maxMovement = maxMovement;
        this.visionRange = visionRange;

        this.currentHealth = maxHealth;
        this.currentMovement = maxMovement;
        this.owner = owner;
    }

    // Getters
    public String getType() { return type; }
    public int getHealth() { return currentHealth; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getCurrentMovement() { return currentMovement; }
    public int getMaxMovement() { return maxMovement; }
    public int getVisionRange() { return visionRange; }
    public Player getOwner() { return owner; }
    public HexagonTile getPosition() { return position; }

    // Setters
    public void setPosition(HexagonTile position) {
        this.position = position;
    }

    // Remise à zéro des points de déplacement au début du tour
    public void resetMovement() {
        this.currentMovement = maxMovement;
    }

    // ✅ Marqueur : cette unité a été attaquée ce tour-ci
    public void setWasAttackedThisTurn(boolean attacked) {
        this.wasAttackedThisTurn = attacked;
    }

    public void setCurrentMovement(int value) {
        this.currentMovement = value;
    }

    public boolean wasAttackedThisTurn() {
        return this.wasAttackedThisTurn;
    }

    // Appliquer des dégâts
    public void receiveDamage(int amount) {
        this.currentHealth -= amount;
        if (this.currentHealth < 0) this.currentHealth = 0;
        this.wasAttackedThisTurn = true; // ✅ important
    }

    // Récupération des PV (10% des PV max) seulement si pas attaqué ce tour
    public void recoverIfIdle() {
        if (!wasAttackedThisTurn && currentHealth < maxHealth) {
            int recovered = (int) Math.ceil(maxHealth * 0.10);
            currentHealth = Math.min(maxHealth, currentHealth + recovered);
        }
        this.wasAttackedThisTurn = false; // ✅ reset en fin de tour
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " [" + type + "] HP:" + currentHealth;
    }
}

package com.wargame.modele;

public enum Terrain {
    PLAINE(0),
    FORET(20),
    MONTAGNE(40),
    FORTERESSE(60),
    RIVIERE(30);

    private final int bonusDefense;

    Terrain(int bonusDefense) {
        this.bonusDefense = bonusDefense;
    }

    public int getBonusDefense() {
        return bonusDefense;
    }
}

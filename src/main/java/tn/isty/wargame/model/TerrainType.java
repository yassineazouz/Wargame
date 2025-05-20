package tn.isty.wargame.model;

import java.io.Serializable;

public enum TerrainType implements Serializable {
    PLAINE(1),
    FORET(2),
    MONTAGNE(3),
    COLLINE(2),
    FORTERESSE(1),
    EAU(999);

    private final int moveCost;

    TerrainType(int moveCost) {
        this.moveCost = moveCost;
    }

    public int getMoveCost() {
        return moveCost;
    }
}
package com.wargame.modele;

public class Hexagone {
    private final int x;
    private final int y;
    private final Terrain terrain;
    private Unite unite;

    public Hexagone(int x, int y, Terrain terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
        this.unite = null;
    }

    public boolean estLibre() {
        return unite == null;
    }

    public void placerUnite(Unite unite) {
        this.unite = unite;
    }

    public void retirerUnite() {
        this.unite = null;
    }

    public Unite getUnite() {
        return unite;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

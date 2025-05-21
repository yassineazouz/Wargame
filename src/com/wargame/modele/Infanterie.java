package com.wargame.modele;

public class Infanterie extends Unite {

    public Infanterie() {
        super("Infanterie", 30, 6, 4, 3, 2);
    }

    @Override
    public void attaquer(Unite cible, Terrain terrain) {
        int bonusDefense = terrain.getBonusDefense();
        int defenseTotale = cible.defense + (int)(cible.defense * (bonusDefense / 100.0));
        int degats = this.attaque - defenseTotale;
        degats += (int)(Math.random() * degats - degats / 2); // variation aléatoire
        cible.pv -= Math.max(degats, 0);
    }

    @Override
    public int getPvInitial() {
        return 30;
    }
}

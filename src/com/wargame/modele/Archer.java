package com.wargame.modele;

public class Archer extends Unite {

    private final int portee;

    public Archer() {
        super("Archer", 20, 4, 2, 4, 3);
        this.portee = 2; // Attaque à distance
    }

    @Override
    public void attaquer(Unite cible, Terrain terrain) {
        // On considère ici que la distance est déjà vérifiée ailleurs
        int bonusDefense = terrain.getBonusDefense();
        int defenseTotale = cible.defense + (int)(cible.defense * (bonusDefense / 100.0));
        int degats = this.attaque - defenseTotale;
        degats += (int)(Math.random() * degats - degats / 2);
        cible.pv -= Math.max(degats, 0);
    }

    @Override
    public int getPvInitial() {
        return 20;
    }

    public int getPortee() {
        return portee;
    }
}

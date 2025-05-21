package com.wargame.modele;

public abstract class Unite {
    protected String nom;
    protected int pv;
    protected int attaque;
    protected int defense;
    protected int deplacement;
    protected int champDeVision;

    public Unite(String nom, int pv, int attaque, int defense, int deplacement, int champDeVision) {
        this.nom = nom;
        this.pv = pv;
        this.attaque = attaque;
        this.defense = defense;
        this.deplacement = deplacement;
        this.champDeVision = champDeVision;
    }

    public int getPv() {
        return this.pv;
    }


    public abstract void attaquer(Unite cible, Terrain terrain);

    public void recuperer() {
        int recup = (int)(0.1 * getPvInitial());
        this.pv = Math.min(this.pv + recup, getPvInitial());
    }

    public boolean estVivant() {
        return pv > 0;
    }

    public abstract int getPvInitial();
}

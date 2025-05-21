package com.wargame.modele;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private final String nom;
    private final List<Unite> unites;

    public Joueur(String nom) {
        this.nom = nom;
        this.unites = new ArrayList<>();
    }

    public void ajouterUnite(Unite u) {
        unites.add(u);
    }

    public boolean aDesUnites() {
        return unites.stream().anyMatch(Unite::estVivant);
    }

    public List<Unite> getUnites() {
        return unites;
    }

    public String getNom() {
        return nom;
    }
}

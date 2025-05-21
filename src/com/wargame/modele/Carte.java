package com.wargame.modele;

import java.util.HashMap;
import java.util.Map;

public class Carte {
    private final Map<String, Hexagone> hexagones;

    public Carte() {
        this.hexagones = new HashMap<>();
    }

    public void ajouterHexagone(Hexagone h) {
        String cle = h.getX() + "," + h.getY();
        hexagones.put(cle, h);
    }

    public Hexagone getHexagone(int x, int y) {
        return hexagones.get(x + "," + y);
    }

    public boolean deplacerUnite(int x1, int y1, int x2, int y2) {
        Hexagone source = getHexagone(x1, y1);
        Hexagone destination = getHexagone(x2, y2);
        if (source != null && destination != null && destination.estLibre()) {
            destination.placerUnite(source.getUnite());
            source.retirerUnite();
            return true;
        }
        return false;
    }
}

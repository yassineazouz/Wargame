package com.wargame;

import com.wargame.modele.*;

public class TestCombat {
    public static void main(String[] args) {
        Infanterie attaquant = new Infanterie();
        Infanterie cible = new Infanterie();

        System.out.println("PV cible avant : " + cible.getPv());
        attaquant.attaquer(cible, Terrain.FORTERESSE);
        System.out.println("PV cible après : " + cible.getPv());
    }
}
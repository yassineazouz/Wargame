package tn.isty.wargame.controller;

import tn.isty.wargame.model.Plateau;

public class GameController {

    public final Plateau plateau;

    public GameController(Plateau plateau) {
        if (plateau == null) {
            throw new IllegalArgumentException("Plateau ne peut pas être nul.");
        }
        this.plateau = plateau;
    }




}

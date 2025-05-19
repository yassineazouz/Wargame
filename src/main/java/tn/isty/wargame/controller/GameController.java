package tn.isty.wargame.controller;

import tn.isty.wargame.model.*;

public class GameController {

    private GameState gameState;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    public void startGame() {
        System.out.println("🎮 Démarrage de la partie !");
        gameState.initializeGame();
        playTurn();
    }

    public void playTurn() {
        Player current = gameState.getCurrentPlayer();
        System.out.println("🔁 Tour du joueur : " + current.getName());

        if (current.isAI()) {
            playAITurn(current);
        } else {
            // Ici, on pourrait écouter des clics UI plus tard
            System.out.println("🕹️  Le joueur humain doit jouer manuellement (non géré ici)");
        }
    }

    public void endTurn() {
        gameState.switchToNextPlayer();
        playTurn();
    }

    public void moveUnit(Unit unit, HexagonTile destination) {
        if (gameState.canMove(unit, destination)) {
            gameState.moveUnit(unit, destination);
            System.out.println(unit.getName() + " s’est déplacé en " + destination);
        } else {
            System.out.println("❌ Déplacement non autorisé");
        }
    }

    public void attack(Unit attacker, Unit defender) {
        if (gameState.canAttack(attacker, defender)) {
            gameState.resolveCombat(attacker, defender);
            System.out.println(attacker.getName() + " attaque " + defender.getName());
        } else {
            System.out.println("❌ Attaque non autorisée");
        }
    }

    public void playAITurn(Player aiPlayer) {
        System.out.println("🤖 Tour de l’IA : " + aiPlayer.getName());
        // TODO plus tard : boucle d’actions automatique
        endTurn();
    }
}
package tn.isty.wargame.controller;

import tn.isty.wargame.model.*;

import java.util.Scanner;

public class GameController {

    private GameState gameState;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    public void startGame() {
        System.out.println("Demarrage de la partie !");
        gameState.initializeGame();
        playTurn();
    }

    public void playTurn() {
        // Sauter les joueurs éliminés
        while (!gameState.getCurrentPlayer().hasAliveUnits()) {
            System.out.println("Le joueur " + gameState.getCurrentPlayer().getName() + " est éliminé.");
            gameState.switchToNextPlayer();
        }

        Player current = gameState.getCurrentPlayer();
        System.out.println("Tour du joueur : " + current.getName());

        if (gameState.isGameOver()) {
            Player winner = gameState.getWinner();
            if (winner != null) {
                System.out.println("🏆 Le joueur " + winner.getName() + " a gagné !");
            } else {
                System.out.println("Match nul !");
            }
            return;
        }

        if (current.isAI()) {
            playAITurn(current);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        for (Unit unit : current.getUnits()) {
            if (!unit.isAlive()) continue;

            System.out.println("Unité : " + unit + " à la case [" + unit.getPosition().getRow() + "," + unit.getPosition().getCol() + "]");
            unit.resetMovement();

            System.out.print("Souhaitez-vous déplacer cette unité ? (o/n) : ");
            String rep = scanner.nextLine();
            if (!rep.equalsIgnoreCase("o")) continue;

            System.out.print("Entrez ligne de destination : ");
            int row = Integer.parseInt(scanner.nextLine());

            System.out.print("Entrez colonne de destination : ");
            int col = Integer.parseInt(scanner.nextLine());

            HexagonTile dest = gameState.getBoard().getCase(row, col);
            if (gameState.canMove(unit, dest)) {
                gameState.moveUnit(unit, dest);
                System.out.println(unit.getName() + " s’est déplacé en [" + row + "," + col + "]");
            } else {
                System.out.println("Déplacement non autorisé.");
            }
        }

        gameState.getBoard().afficherConsole();
        endTurn();
    }


    public void endTurn() {
        gameState.switchToNextPlayer();
        playTurn();
    }

    public void moveUnit(Unit unit, HexagonTile destination) {
        if (gameState.canMove(unit, destination)) {
            gameState.moveUnit(unit, destination);
            System.out.println(unit.getName() + " s’est déplacé.");
        } else {
            System.out.println("Déplacement non autorisé.");
        }
    }

    public void attack(Unit attacker, Unit defender) {
        if (gameState.canAttack(attacker, defender)) {
            gameState.resolveCombat(attacker, defender);
            System.out.println(attacker.getName() + " attaque " + defender.getName() + ".");
        } else {
            System.out.println("Attaque non autorisée.");
        }
    }

    public void playAITurn(Player aiPlayer) {
        if (gameState.isGameOver()) {
            Player winner = gameState.getWinner();
            if (winner != null) {
                System.out.println("Le joueur " + winner.getName() + " a gagné !");
            } else {
                System.out.println("Match nul.");
            }
            return;
        }

        Player current = gameState.getCurrentPlayer();
        System.out.println("Tour du joueur : " + current.getName());

        for (Unit unit : current.getUnits()) {
            if (!unit.isAlive()) continue;

            unit.resetMovement();

            HexagonTile pos = unit.getPosition();

            // Attaque si ennemi à proximité
            boolean attacked = false;
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;

                    HexagonTile cible = gameState.getBoard().getCase(pos.getRow() + dr, pos.getCol() + dc);
                    if (cible != null && cible.getUnit() != null && gameState.canAttack(unit, cible.getUnit())) {
                        gameState.resolveCombat(unit, cible.getUnit());
                        attacked = true;
                        break;
                    }
                }
                if (attacked) break;
            }

            // Sinon déplacement simple vers le bas
            if (!attacked) {
                HexagonTile dest = gameState.getBoard().getCase(pos.getRow() + 1, pos.getCol());
                if (gameState.canMove(unit, dest)) {
                    gameState.moveUnit(unit, dest);
                    System.out.println(unit.getName() + " se déplace.");
                }
            }
        }

        gameState.getBoard().afficherConsole();
        gameState.switchToNextPlayer();
        playTurn(); // Tour suivant
    }

}

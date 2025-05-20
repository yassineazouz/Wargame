package tn.isty.wargame.controller;

import tn.isty.wargame.model.GameState;
import tn.isty.wargame.model.HexagonTile;
import tn.isty.wargame.model.Player;
import tn.isty.wargame.model.Unit;

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
        gameState.getBoard().refreshVisibility();

        if (current.isAI()) {
            playAITurn(current);
        } else {
            System.out.println("🕹️ Le joueur humain doit jouer manuellement");
            // ❌ Ne plus dire "non géré ici"
        }
    }

    public void endTurn() {
        Player currentPlayer = gameState.getCurrentPlayer();

        for (Unit unit : currentPlayer.getUnits()) {
            if (unit.isAlive() && unit.getCurrentMovement() == unit.getMaxMovement()) {
                unit.recoverIfIdle();
                System.out.println("🔧 " + unit.getName() + " récupère des PV (repos)");
            }
        }

        gameState.switchToNextPlayer();

        if (gameState.isGameOver()) {
            Player winner = gameState.getWinner();
            if (winner != null) {
                System.out.println("🎉 Partie terminée ! Gagnant : " + winner.getName());
            } else {
                System.out.println("🎯 Match nul !");
            }
            return;
        }

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
            HexagonTile tile = defender.getPosition();
            if (tile != null) tile.playAttackAnimation();

            gameState.resolveCombat(attacker, defender);
            System.out.println(attacker.getName() + " attaque " + defender.getName());
        } else {
            System.out.println("❌ Attaque non autorisée");
        }
    }

    public void playAITurn(Player aiPlayer) {
        System.out.println("🤖 Tour de l’IA : " + aiPlayer.getName());

        for (Unit aiUnit : aiPlayer.getUnits()) {
            if (!aiUnit.isAlive()) continue;

            // 1. Cherche une cible attaquable
            for (Player opponent : gameState.getAllPlayers()) {
             if (opponent == aiPlayer) continue;

                for (Unit target : opponent.getUnits()) {
                   if (!target.isAlive()) continue;

                   if (gameState.canAttack(aiUnit, target)) {
                       System.out.println("🤖 IA attaque avec " + aiUnit.getName() + " -> " + target.getName());
                       attack(aiUnit, target);
                       return; // Une action par tour
                   }
              }
          }

            // 2. Déplacement vers l'ennemi le plus proche
            Unit closest = null;
            int minDist = Integer.MAX_VALUE;
            for (Player opponent : gameState.getAllPlayers()) {
                if (opponent == aiPlayer) continue;
                for (Unit enemy : opponent.getUnits()) {
                   if (!enemy.isAlive()) continue;

                  int dist = gameState.calculerDistanceHex(aiUnit.getPosition(), enemy.getPosition());
                  if (dist < minDist) {
                      minDist = dist;
                      closest = enemy;
                    }
                }
           }

            if (closest != null) {
                HexagonTile from = aiUnit.getPosition();
                HexagonTile to = findStepToward(from, closest.getPosition(), aiUnit.getCurrentMovement());

                if (to != null && gameState.canMove(aiUnit, to)) {
                    System.out.println("🤖 IA déplace " + aiUnit.getName());
                    moveUnit(aiUnit, to);
                    return; // Une action par tour
                }
            }

            // 3. Si rien à faire
           System.out.println("🤖 " + aiUnit.getName() + " reste sur place.");
        }

        endTurn(); // ➡️ Tour terminé
    }

    private HexagonTile findStepToward(HexagonTile from, HexagonTile to, int maxMove) {
        int fromRow = from.getRow(), fromCol = from.getCol();
        int toRow = to.getRow(), toCol = to.getCol();

        // Mouvement le plus simple vers la cible
        int dr = Integer.compare(toRow, fromRow);
        int dc = Integer.compare(toCol, fromCol);

        int newRow = fromRow + dr;
        int newCol = fromCol + dc;

        HexagonTile step = gameState.getBoard().getCase(newRow, newCol);
        return step;
    }
}

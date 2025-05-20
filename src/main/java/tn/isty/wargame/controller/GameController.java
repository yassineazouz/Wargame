package tn.isty.wargame.controller;

import tn.isty.wargame.model.GameState;
import tn.isty.wargame.model.HexagonTile;
import tn.isty.wargame.model.Player;
import tn.isty.wargame.model.TerrainType;
import tn.isty.wargame.model.Unit;

public class GameController {

    private final GameState gameState;

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
            System.out.println(winner != null
                    ? "🎉 Partie terminée ! Gagnant : " + winner.getName()
                    : "🎯 Match nul !");
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

        // 1. Essayer d'attaquer une unité ennemie
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

        // 2. Trouver l’ennemi vivant le plus proche
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

        // 3. Se déplacer vers la cible
        if (closest != null) {
            HexagonTile from = aiUnit.getPosition();
            HexagonTile to = findStepToward(from, closest.getPosition(), aiUnit.getCurrentMovement());

            if (to != null && gameState.canMove(aiUnit, to)) {
                TerrainType terrain = to.getTerrainType();
                if (terrain.getMoveCost() >= 999) {
                    System.out.println("🌊 IA évite terrain interdit (eau)");
                    continue;
                }

                System.out.println("🤖 IA déplace " + aiUnit.getName() + " vers " + to.getRow() + "," + to.getCol());
                moveUnit(aiUnit, to);
                return; // Une action par tour
            }
        }

        // 4. Si rien à faire
        System.out.println("🤖 " + aiUnit.getName() + " reste sur place.");
    }

    endTurn(); // ➡️ Fin du tour IA
}


    private HexagonTile findStepToward(HexagonTile from, HexagonTile to, int maxMove) {
        int fromRow = from.getRow(), fromCol = from.getCol();
        int toRow = to.getRow(), toCol = to.getCol();

        int dr = Integer.compare(toRow, fromRow);
        int dc = Integer.compare(toCol, fromCol);

        int newRow = fromRow + dr;
        int newCol = fromCol + dc;

        return gameState.getBoard().getCase(newRow, newCol);
    }
}

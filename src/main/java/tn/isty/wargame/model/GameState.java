package tn.isty.wargame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Player> players;
    private int currentPlayerIndex;
    private Plateau board;

    public GameState(List<Player> players, Plateau board) {
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = 0;
    }

    public void initializeGame() {
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                unit.resetMovement();
                unit.setWasAttackedThisTurn(false); // Initialiser à false au début
            }
        }
        board.refreshVisibility();
    }

    public void switchToNextPlayer() {
        Player currentPlayer = getCurrentPlayer();

        for (Unit unit : currentPlayer.getUnits()) {
            if (unit.isAlive() && unit.getCurrentMovement() == unit.getMaxMovement()) {
                if (!unit.wasAttackedThisTurn()) {
                    unit.recoverIfIdle();
                    System.out.println("🔧 " + unit.getName() + " récupère des PV (repos)");
                }
            }
            // Réinitialiser pour le tour suivant
            unit.setWasAttackedThisTurn(false);
            unit.resetMovement();
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        board.refreshVisibility();
    }

    public boolean isGameOver() {
        long remainingPlayers = players.stream()
                .filter(player -> player.getUnits().stream().anyMatch(Unit::isAlive))
                .count();
        return remainingPlayers <= 1;
    }

    public Player getWinner() {
        return players.stream()
                .filter(player -> player.getUnits().stream().anyMatch(Unit::isAlive))
                .findFirst()
                .orElse(null);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Player> getAllPlayers() {
        return players;
    }

    public Plateau getBoard() {
        return board;
    }

    public boolean canMove(Unit unit, HexagonTile destination) {
        return unit.isAlive() && board.isReachable(unit.getPosition(), destination, unit.getCurrentMovement());
    }
    public void moveUnit(Unit unit, HexagonTile destination) {
        int distance = calculerDistanceHex(unit.getPosition(), destination);

        // Supprimer l'unité de l'ancienne case
        HexagonTile from = unit.getPosition();
        if (from != null) from.setUnit(null);

        // Mettre à jour la position
        unit.setPosition(destination);
        destination.setUnit(unit);

        // Décrémenter les points de mouvement
        unit.setCurrentMovement(unit.getCurrentMovement() - distance);

        // 🔄 Mise à jour visuelle immédiate
        destination.updateDisplay();
        if (from != null) from.updateDisplay();
    }


    public boolean canAttack(Unit attacker, Unit defender) {
        return attacker.isAlive()
                && defender.isAlive()
                && attacker.getOwner() != defender.getOwner()
                && calculerDistanceHex(attacker.getPosition(), defender.getPosition()) <= 1;
    }

    public void resolveCombat(Unit attacker, Unit defender) {
        int baseDamage = attacker.getAttack() - defender.getDefense();
        int terrainModifier = 0; // à implémenter si besoin
        int randomFactor = (int) (Math.random() * 5) - 2; // [-2, +2]

        int totalDamage = Math.max(1, baseDamage + terrainModifier + randomFactor);
        defender.receiveDamage(totalDamage);
        defender.setWasAttackedThisTurn(true); // ✅ Marque l'unité comme attaquée ce tour
        System.out.println("💥 Dégâts infligés : " + totalDamage +
            " (base: " + baseDamage + ", terrain: " + terrainModifier + ", hasard: " + randomFactor + ")");
        System.out.println("❤️ PV restants de " + defender.getName() + " : " + defender.getHealth());
    }

        // 🔁 MODIFICATION de calculerDistanceHex :
    public int calculerDistanceHex(HexagonTile a, HexagonTile b) {
        int colA = a.getCol();
        int rowA = a.getRow() - (a.getCol() - (a.getCol() & 1)) / 2;

        int colB = b.getCol();
        int rowB = b.getRow() - (b.getCol() - (b.getCol() & 1)) / 2;

        int dx = colA - colB;
        int dy = rowA - rowB;

        return (Math.abs(dx) + Math.abs(dy) + Math.abs(dx + dy)) / 2;
        }


    public void setBoard(Plateau board) {
    this.board = board;
}

}

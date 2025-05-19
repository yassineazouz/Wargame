package tn.isty.wargame.model;
import java.util.List;

public class GameState {
    private List<Player> players;
    private int currentPlayerIndex;
    private Plateau board;

    public GameState(List<Player> players, Plateau board) {
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void initializeGame() {
        // Prévu dans Main.java
    }

    public Plateau getBoard() {
        return board;
    }

    public boolean canMove(Unit unit, HexagonTile to) {
        if (unit == null || to == null) return false;
        if (!unit.isAlive() || to.getUnit() != null) return false;

        HexagonTile from = unit.getPosition();
        if (from == null) return false;

        int distance = calculerDistanceHex(from, to);
        return distance <= unit.getCurrentMovement();
    }

    public void moveUnit(Unit unit, HexagonTile to) {
        if (!canMove(unit, to)) return;

        HexagonTile from = unit.getPosition();
        from.setUnit(null);

        to.setUnit(unit);
        unit.setPosition(to);

        unit.resetMovement(); // Reset movement if needed
    }

    public boolean canAttack(Unit attacker, Unit defender) {
        if (attacker == null || defender == null) return false;
        if (!attacker.isAlive() || !defender.isAlive()) return false;
        if (attacker.getOwner() == defender.getOwner()) return false;

        int distance = calculerDistanceHex(attacker.getPosition(), defender.getPosition());
        return distance <= attacker.getVisionRange(); // attaque à portée
    }

    public void resolveCombat(Unit attacker, Unit defender) {
        int baseDamage = attacker.getAttack() - defender.getDefense();
        baseDamage = Math.max(1, baseDamage);

        TerrainType terrain = defender.getPosition().getTerrainType();
        int terrainBonus = 0;
        switch (terrain) {
            case FORET:
            terrainBonus = 1;
            break;
        case COLLINE:
            terrainBonus = 2;
            break;
        case MONTAGNE:
            terrainBonus = 3;
            break;
        case FORTERESSE:
            terrainBonus = 4;
            break;
        default:
            terrainBonus = 0;
}


        baseDamage -= terrainBonus;
        baseDamage = Math.max(1, baseDamage);

        int randomBonus = (int) (Math.random() * 5) - 2; // -2 à +2
        int totalDamage = Math.max(1, baseDamage + randomBonus);

        defender.receiveDamage(totalDamage);

        System.out.println("🗡 " + attacker.getName() + " attaque " + defender.getName());
        System.out.println("💥 Dégâts infligés : " + totalDamage + " (base: " + baseDamage + ", terrain: -" + terrainBonus + ", hasard: " + randomBonus + ")");
        System.out.println("❤️ PV restants de " + defender.getName() + " : " + defender.getHealth());

        if (!defender.isAlive()) {
            HexagonTile tile = defender.getPosition();
            tile.setUnit(null);
            defender.setPosition(null);
            System.out.println("☠ " + defender.getName() + " est mort !");
        }
    }

    public boolean isGameOver() {
        return false;
    }

    public Player getWinner() {
        return null;
    }

    private int calculerDistanceHex(HexagonTile a, HexagonTile b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}

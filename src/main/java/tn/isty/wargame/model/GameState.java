
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

    // Récupère le joueur qui doit jouer
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Passe au joueur suivant
    public void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    //  Initialise la partie (placement des unités, etc.)
    public void initializeGame() {
        int totalPlayers = players.size();
        int midRow = board.getRows() / 2;

        for (int i = 0; i < totalPlayers; i++) {
            Player player = players.get(i);

            int baseRow;
            switch (i) {
                case 0 -> baseRow = 0;
                case 1 -> baseRow = board.getRows() - 1;
                case 2 -> baseRow = midRow; // centre
                case 3 -> baseRow = midRow + 1; // juste sous le centre
                default -> baseRow = 1; // fallback
            }

            for (int j = 0; j < 2; j++) {
                int col = 1 + j * 2; // colonne 1 et 3
                Unit unit = new Unit("P" + (i + 1) + " - Inf" + (j + 1), "infanterie", 20, 5 - j, 2, 3, 3, player);
                board.placerUnite(baseRow, col, unit);
                player.addUnit(unit);
            }
        }

        board.afficherConsole();
    }

    public Plateau getBoard() {
        return board;
    }

    // À implémenter ensuite (on les prépare)
    public boolean canMove(Unit unit, HexagonTile to) {
        if (unit == null || to == null) return false;
        if (!unit.isAlive()) return false;
        if (to.getUnit() != null) return false;

        HexagonTile from = unit.getPosition();
        if (from == null) return false;

        int distance = calculerDistanceHex(from, to);
        return distance <= unit.getCurrentMovement();
    }


    public void moveUnit(Unit unit, HexagonTile to) {
        HexagonTile from = unit.getPosition();
        if (from != null) {
            from.setUnit(null); // vider la case précédente
        }

        to.setUnit(unit); // placer dans la nouvelle case
        unit.setPosition(to);

        int distance = calculerDistanceHex(from, to);
        unit.setCurrentMovement(unit.getCurrentMovement() - distance); // décrémenter
    }

    public boolean canAttack(Unit attacker, Unit defender) {
        if (attacker == null || defender == null) return false;
        if (!attacker.isAlive() || !defender.isAlive()) return false;
        if (attacker.getOwner() == defender.getOwner()) return false;

        HexagonTile from = attacker.getPosition();
        HexagonTile to = defender.getPosition();
        if (from == null || to == null) return false;

        int distance = calculerDistanceHex(from, to);
        return distance <= attacker.getVisionRange(); // ou portée d’attaque si tu as une variable dédiée
    }

    public void resolveCombat(Unit attacker, Unit defender) {
        int attaque = attacker.getAttack();
        int defense = defender.getDefense();

        // Bonus terrain optionnel
        TerrainType terrain = defender.getPosition().getTerrainType();
        if (terrain == TerrainType.FORTERESSE) {
            defense += 2;
        }

        // Hasard simple entre -1 et +1
        int random = (int)(Math.random() * 3) - 1;

        int damage = Math.max(1, attaque - defense + random);
        defender.receiveDamage(damage);

        System.out.println(attacker.getName() + " inflige " + damage + " points de degats à " + defender.getName());

        if (!defender.isAlive()) {
            System.out.println(defender.getName() + " est éliminé.");
            defender.getPosition().setUnit(null); // retirer du plateau
        }
    }

    public boolean isGameOver() {
        int count = 0;
        for (Player player : players) {
            if (player.hasAliveUnits()) {
                count++;
            }
        }
        return count <= 1;
    }

    public Player getWinner() {
        for (Player player : players) {
            if (player.hasAliveUnits()) {
                return player;
            }
        }

        return null;
    }

    private int calculerDistanceHex(HexagonTile a, HexagonTile b) {
        //  méthode simplifiée en mode grille : à améliorer plus tard pour grille hexagonale
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    private int getTerrainCost(TerrainType type) {
        return switch (type) {
            case PLAINE -> 1;
            case FORET, COLLINE -> 2;
            case MONTAGNE, FORTERESSE -> 3;
        };
    }

    public List<Unit> getEnemyUnits(Player player) {
        return players.stream()
                .filter(p -> !p.equals(player))
                .flatMap(p -> p.getUnits().stream())
                .filter(Unit::isAlive)
                .toList();
    }


}

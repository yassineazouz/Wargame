
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
        // TODO : positionner les unités sur le plateau
        // Exemple : board.placeUnit(...), player.addUnit(...)
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
        // TODO : déplacer l’unité et décrémenter ses points de déplacement
    }

    public boolean canAttack(Unit attacker, Unit defender) {
        // TODO : vérifier portée, camps opposés, état vivant
        return true;
    }

    public void resolveCombat(Unit attacker, Unit defender) {
        // TODO : appliquer le calcul des dégâts avec bonus de terrain et hasard
    }

    public boolean isGameOver() {
        // TODO : vérifier conditions de victoire
        return false;
    }

    public Player getWinner() {
        // TODO : retourner le joueur vainqueur s’il y en a un
        return null;
    }

    private int calculerDistanceHex(HexagonTile a, HexagonTile b) {
        // ⚠️ méthode simplifiée en mode grille : à améliorer plus tard pour grille hexagonale
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

}

package tn.isty.wargame.model;

import java.io.Serializable;
import javafx.scene.layout.Pane;

public class Plateau extends Pane implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int rows;
    private final int cols;
    private final double tileSize = 40;
    private HexagonTile[][] grille;

    public Plateau(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.setPrefSize(1920, 1080);

        generatePlateau();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void generatePlateau() {
        double hexHeight = tileSize * 2;
        double hexWidth = Math.sqrt(3) * tileSize;

        grille = new HexagonTile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TerrainType type = TerrainType.PLAINE;

                HexagonTile hex = new HexagonTile(type, row, col);

                double x = col * hexWidth * 0.75;
                double y = row * hexHeight + (col % 2) * (hexHeight / 2);

                hex.setLayoutX(x);
                hex.setLayoutY(y);

                this.getChildren().add(hex);
                grille[row][col] = hex;
            }
        }
    }

    public HexagonTile getCase(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grille[row][col];
        } else {
            return null;
        }
    }

    public void placerUnite(int row, int col, Unit unite) {
        HexagonTile tile = getCase(row, col);
        if (tile != null && tile.getUnit() == null) {
            tile.setUnit(unite);
            unite.setPosition(tile);
        }
    }

    public void afficherConsole() {
        System.out.println("=== Plateau de jeu ===");

        for (int row = 0; row < rows; row++) {
            if (row % 2 != 0) System.out.print("  ");

            for (int col = 0; col < cols; col++) {
                HexagonTile tile = grille[row][col];
                Unit u = tile.getUnit();

                if (u != null) {
                    System.out.print("[" + u.getName().charAt(0) + "] ");
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }

    public void refreshVisibility() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grille[row][col].updateDisplay();
            }
        }
    }

    // ✅ MÉTHODES AJOUTÉES

    public boolean isReachable(HexagonTile from, HexagonTile to, int movementPoints) {
        if (from == null || to == null) return false;
        int distance = calculerDistance(from, to);
        return distance <= movementPoints;
    }
    public boolean isVisible(HexagonTile tile) {
        if (tile == null) return false;

        GameState gameState = HexagonTile.getSharedGameState();
        if (gameState == null) return false;

        Player currentPlayer = gameState.getCurrentPlayer();
        for (Player p : gameState.getAllPlayers()) {
            for (Unit unit : p.getUnits()) {
                if (unit.getOwner().equals(currentPlayer)) {
                    HexagonTile pos = unit.getPosition();
                    if (pos == null) continue;  // ✅ Évite NullPointerException
                    if (calculerDistance(pos, tile) <= unit.getVisionRange()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int hexDistance(HexagonTile a, HexagonTile b) {
        int colA = a.getCol();
        int rowA = a.getRow() - (a.getCol() - (a.getCol() & 1)) / 2;

        int colB = b.getCol();
        int rowB = b.getRow() - (b.getCol() - (b.getCol() & 1)) / 2;

        int dx = colA - colB;
        int dy = rowA - rowB;

        return (Math.abs(dx) + Math.abs(dy) + Math.abs(dx + dy)) / 2;
    }

    // 🔁 MODIFICATION de calculerDistance → remplacer le contenu
    private int calculerDistance(HexagonTile a, HexagonTile b) {
        return hexDistance(a, b); // ⬅️ Redirection vers la vraie méthode
    }

}

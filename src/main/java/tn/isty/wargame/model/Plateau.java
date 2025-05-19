package tn.isty.wargame.model;

import javafx.scene.layout.Pane;

public class Plateau extends Pane {

    private final int rows;
    private final int cols;
    private final double tileSize = 40; // Rayon (distance centre → sommet)
    private HexagonTile[][] grille;  //Grille logique des cases

    public Plateau(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.setPrefSize(1920, 1080);

        generatePlateau();
    }

    private void generatePlateau() {
        double hexHeight = tileSize * 2; // Hauteur d’un hexagone (diamètre)
        double hexWidth = Math.sqrt(3) * tileSize; // Largeur

        grille = new HexagonTile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TerrainType type = TerrainType.PLAINE; // ou getRandomTerrain() pour plus tard

                HexagonTile hex = new HexagonTile(type , row, col);

                // Placement en grille décalée (offset selon la colonne)
                double x = col * hexWidth * 0.75;
                double y = row * hexHeight + (col % 2) * (hexHeight / 2);

                hex.setLayoutX(x);
                hex.setLayoutY(y);

                this.getChildren().add(hex);
                grille[row][col] = hex;
            }
        }
    }
    // Accès à une case (avec sécurité)
    public HexagonTile getCase(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grille[row][col];
        } else {
            return null;
        }
    }
    // Placer une unité dans une case donnée
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
            // décalage visuel (grille hexagonale)
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


}

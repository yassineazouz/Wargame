package tn.isty.wargame.model;

import javafx.scene.layout.Pane;

public class Plateau extends Pane {

    private final int rows;
    private final int cols;
    private final double tileSize = 40; // Rayon (distance centre → sommet)

    public Plateau(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.setPrefSize(1920, 1080);

        generatePlateau();
    }

    private void generatePlateau() {
        double hexHeight = tileSize * 2; // Hauteur d’un hexagone (diamètre)
        double hexWidth = Math.sqrt(3) * tileSize; // Largeur

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TerrainType type = TerrainType.PLAINE; // ou getRandomTerrain() pour plus tard

                HexagonTile hex = new HexagonTile(type);

                // Placement en grille décalée (offset selon la colonne)
                double x = col * hexWidth * 0.75;
                double y = row * hexHeight + (col % 2) * (hexHeight / 2);

                hex.setLayoutX(x);
                hex.setLayoutY(y);

                this.getChildren().add(hex);
            }
        }
    }
}

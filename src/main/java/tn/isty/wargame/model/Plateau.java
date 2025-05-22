package tn.isty.wargame.model;

import javafx.scene.layout.Pane;

public class Plateau extends Pane {
    private TerrainType[][] terrainGrid;

    private static final int DEFAULT_ROWS = 20;
    private static final int DEFAULT_COLS = 30;

    public Plateau() {
        // On initialise avec des dimensions par défaut
        terrainGrid = new TerrainType[DEFAULT_ROWS][DEFAULT_COLS];

        // Redessine le terrain quand la taille de la fenêtre change
        widthProperty().addListener((obs, oldVal, newVal) -> afficherTerrain());
        heightProperty().addListener((obs, oldVal, newVal) -> afficherTerrain());
    }

    public void generateIsland() {
        int rows = getRows();
        int cols = getCols();


        int centerX = rows / 2;
        int centerY = cols / 2;
        int radius = Math.min(rows, cols) / 3;

        terrainGrid = new TerrainType[rows][cols]; // Recréer la grille

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double dx = row - centerX;
                double dy = col - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < radius * 0.6) {
                    terrainGrid[row][col] = TerrainType.PLAINE;
                } else if (distance < radius * 0.8) {
                    terrainGrid[row][col] = TerrainType.FORET;
                } else if (distance < radius) {
                    terrainGrid[row][col] = TerrainType.COLLINE;
                } else {
                    terrainGrid[row][col] = TerrainType.EAU;
                }
            }
        }

        terrainGrid[centerX][centerY] = TerrainType.FORTERESSE;
        afficherTerrain();
    }

    public void generateCityTerrain() {

        int rows = getRows();
        int cols = getCols();


        double centerX = rows / 2.0;
        double centerY = cols / 2.0;
        double minDim = Math.min(rows, cols) / 4.0;

        terrainGrid = new TerrainType[rows][cols]; // Recréer la grille

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double dx = row - centerX;
                double dy = col - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < minDim * 0.6) {
                    terrainGrid[row][col] = TerrainType.FORTERESSE;
                } else if (dist < minDim * 0.8) {
                    terrainGrid[row][col] = TerrainType.FORET;
                } else {
                    terrainGrid[row][col] = TerrainType.PLAINE;
                }
            }
        }

        terrainGrid[(int) centerX][(int) centerY] = TerrainType.FORTERESSE;
        afficherTerrain();
    }

    private void afficherTerrain() {
        if (terrainGrid == null) return;

        this.getChildren().clear();

        double hexHeight = HexagonTile.SIZE * 2;
        double hexWidth = Math.sqrt(3) * HexagonTile.SIZE;

        for (int row = 0; row < terrainGrid.length; row++) {
            for (int col = 0; col < terrainGrid[0].length; col++) {
                TerrainType terrain = terrainGrid[row][col];
                if (terrain == null) continue;

                double x = col * hexWidth;
                double y = row * (hexHeight * 0.75);

                if (col % 2 == 1) {
                    y += hexHeight * 0.5;
                }

                HexagonTile hex = new HexagonTile(0, 0, terrain);
                hex.setLayoutX(x);
                hex.setLayoutY(y);

                this.getChildren().add(hex);
            }
        }
    }



    public int getRows() {
        return terrainGrid.length;
    }

    public int getCols() {
        return terrainGrid[0].length;
    }
}

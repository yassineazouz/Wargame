package tn.isty.wargame.view;

import javafx.scene.layout.Pane;
import tn.isty.wargame.model.HexagonTile;
import tn.isty.wargame.model.TerrainType;

import java.util.Random;

public class GameBoard extends Pane {
    private static final int ROWS = 18;
    private static final int COLS = 27;
    private static final double HEX_SIZE = 40;

    private final Random random = new Random();

    public GameBoard() {
        // Créer un tableau 2D pour stocker les types de terrain
        TerrainType[][] terrainGrid = generateTerrainGrid();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // Récupérer le type de terrain depuis la grille
                TerrainType terrainType = terrainGrid[row][col];
                HexagonTile hex = new HexagonTile(terrainType);

                // Calcul position : décalage sur les lignes impaires
                double x = HEX_SIZE * Math.sqrt(3) * (col + 0.5 * (row % 2));
                double y = HEX_SIZE * 1.5 * row;

                hex.setLayoutX(x);
                hex.setLayoutY(y);
                this.getChildren().add(hex);
            }
        }
    }

    private TerrainType[][] generateTerrainGrid() {
        TerrainType[][] grid = new TerrainType[ROWS][COLS];
        Random random = new Random();

        // Probabilités pour chaque type de terrain
        double[] probabilities = {0.4, 0.2, 0.15, 0.15, 0.1}; // PLAINE, FORET, etc.

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                double rand = random.nextDouble();
                double cumulativeProbability = 0.0;

                if (rand < (cumulativeProbability += probabilities[0])) {
                    grid[row][col] = TerrainType.PLAINE;
                } else if (rand < (cumulativeProbability += probabilities[1])) {
                    grid[row][col] = TerrainType.FORET;
                } else if (rand < (cumulativeProbability += probabilities[2])) {
                    grid[row][col] = TerrainType.COLLINE;
                } else if (rand < (cumulativeProbability += probabilities[3])) {
                    grid[row][col] = TerrainType.MONTAGNE;
                } else {
                    grid[row][col] = TerrainType.FORTERESSE;
                }
            }
        }

        return grid;
    }

    private TerrainType getRandomTerrain() {
        TerrainType[] types = TerrainType.values();
        return types[random.nextInt(types.length)];
    }
}

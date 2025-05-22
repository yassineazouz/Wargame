package tn.isty.wargame.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexagonTile extends Polygon {

    public static final double SIZE = 40;
    private static final double SQRT3 = Math.sqrt(3);

    private final TerrainType terrainType;

    public HexagonTile(double x, double y, TerrainType terrainType) {
        // Définir les points de l'hexagone
        this.getPoints().addAll(
                x + SIZE, y,
                x + SIZE / 2, y + SQRT3 * SIZE / 2,
                x - SIZE / 2, y + SQRT3 * SIZE / 2,
                x - SIZE, y,
                x - SIZE / 2, y - SQRT3 * SIZE / 2,
                x + SIZE / 2, y - SQRT3 * SIZE / 2
        );

        this.terrainType = terrainType;

        // Appliquer la couleur
        this.setFill(getColorForTerrain());
        this.setStroke(Color.BLACK);
    }

    private Color getColorForTerrain() {
        switch (terrainType) {
            case PLAINE:
                return Color.LIGHTGREEN;
            case FORET:
                return Color.DARKGREEN;
            case MONTAGNE:
                return Color.DIMGRAY;
            case COLLINE:
                return Color.SANDYBROWN;
            case FORTERESSE:
                return Color.DARKRED;
            case EAU:
                return Color.DODGERBLUE;
            default:
                return Color.GRAY;
        }
    }


}

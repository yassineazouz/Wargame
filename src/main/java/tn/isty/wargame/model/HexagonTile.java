package tn.isty.wargame.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexagonTile extends StackPane {
    private static final double SIZE = 40;
    private TerrainType terrainType;

    public HexagonTile(TerrainType type) {
        this.terrainType = type;

        // Création de l'hexagone
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            double x = SIZE * Math.cos(angle);
            double y = SIZE * Math.sin(angle);
            hex.getPoints().addAll(x, y);
        }

        hex.setStroke(Color.BLACK);
        hex.setFill(getColorForTerrain(type)); // Utilise une couleur spécifique

        this.setPrefSize(SIZE * 2, SIZE * 2);
        this.getChildren().add(hex);
    }

    private Color getColorForTerrain(TerrainType type) {
        switch (type) {
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
            default:
                return Color.GRAY;
        }
    }
}

package tn.isty.wargame.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexagonTile extends StackPane {
    private static final double SIZE = 40;
    private TerrainType terrainType;
    private Unit unit;
    private int row;
    private int col;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public HexagonTile(TerrainType type, int row, int col) {
        this.terrainType = type;
        this.row = row;
        this.col = col;

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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    @Override
    public String toString() {
        return "Hex[" + row + "," + col + "] - " + terrainType + (unit != null ? " with " + unit.getName() : "");
    }

}

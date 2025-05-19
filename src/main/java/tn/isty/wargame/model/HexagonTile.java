package tn.isty.wargame.model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.animation.FillTransition;
import javafx.util.Duration;
import javafx.scene.shape.Shape;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class HexagonTile extends StackPane {
    private static final double SIZE = 40;
    private TerrainType terrainType;
    private Unit unit;
    private int row;
    private int col;
    private Label unitLabel = new Label();

    private static Unit selectedUnit = null;
    private static GameState sharedGameState = null;

    public static void setSharedGameState(GameState gameState) {
        sharedGameState = gameState;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        updateDisplay();
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public HexagonTile(TerrainType type, int row, int col) {
        this.terrainType = type;
        this.row = row;
        this.col = col;

        // Dessin de l'hexagone
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            double x = SIZE * Math.cos(angle);
            double y = SIZE * Math.sin(angle);
            hex.getPoints().addAll(x, y);
        }

        hex.setStroke(Color.BLACK);
        hex.setFill(getColorForTerrain(type));

        this.setPrefSize(SIZE * 2, SIZE * 2);
        this.getChildren().add(hex);

        unitLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        unitLabel.setMouseTransparent(true); // 👈 ligne à ajouter pour que les clics passent au fond
        this.getChildren().add(unitLabel);

        updateDisplay();

        this.setOnMouseClicked(event -> {
        System.out.println("🖱️ Clic détecté sur tuile [" + row + "," + col + "]");

        if (sharedGameState == null) {
            System.out.println("❌ sharedGameState est null");
            return;
        }

        if (unit != null) {
            System.out.println("👉 Tuile occupée par " + unit.getName() + " (" + unit.getOwner().getName() + ")");
        } else {
            System.out.println("👉 Tuile vide");
        }

        Player current = sharedGameState.getCurrentPlayer();
        System.out.println("🔁 Joueur courant : " + current.getName());

        if (unit != null && unit.getOwner().equals(current)) {
            selectedUnit = unit;
            System.out.println("✅ Sélection : " + unit.getName());
        } else if (selectedUnit != null && unit != null && !unit.getOwner().equals(current)) {
            System.out.println("⚔️ Candidat pour attaque : " + unit.getName());
            if (sharedGameState.canAttack(selectedUnit, unit)) {
                System.out.println("💥 Attaque déclenchée !");
                sharedGameState.resolveCombat(selectedUnit, unit);
                this.playAttackAnimation();
                selectedUnit = null;
            } else {
                System.out.println("❌ Attaque non valide");
           }
        } else {
            System.out.println("🟦 Clic ignoré (aucune action possible)");
        }
});

    }

    private Color getColorForTerrain(TerrainType type) {
        Color color;
        switch (type) {
            case PLAINE:
                color = Color.LIGHTGREEN;
                break;
            case FORET:
                color = Color.DARKGREEN;
                break;
            case MONTAGNE:
                color = Color.DIMGRAY;
                break;
            case COLLINE:
                color = Color.SANDYBROWN;
                break;
            case FORTERESSE:
                color = Color.DARKRED;
                break;
            default:
                color = Color.GRAY;
        }
        return color;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void playAttackAnimation() {
        Shape hexShape = (Shape) getChildren().get(0);
        FillTransition ft = new FillTransition(Duration.millis(300), hexShape);
        ft.setFromValue(Color.RED);
        ft.setToValue(getColorForTerrain(this.terrainType));
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();
    }

    public void updateDisplay() {
        if (unit != null) {
            String text = unit.getName() + " (" + unit.getType() + ")";
            unitLabel.setText(text);

            if (unit.getOwner().getName().equalsIgnoreCase("Ghassen")) {
                unitLabel.setTextFill(Color.BLUE);
            } else {
                unitLabel.setTextFill(Color.CRIMSON);
            }

            Tooltip tooltip = new Tooltip("PV : " + unit.getHealth());
            Tooltip.install(this, tooltip);

        } else {
            unitLabel.setText("");
            Tooltip.uninstall(this, null);

        }
    }
}

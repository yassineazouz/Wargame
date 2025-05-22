package tn.isty.wargame.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.isty.wargame.controller.GameController;
import tn.isty.wargame.model.Plateau;

import java.net.URL;

public class GameSetup {

    // Méthode publique pour créer la scène initiale
    public static Scene createSetupScene(Stage stage) {
        Label label = new Label("Choisissez le nombre de joueurs");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        VBox playerSelectionBox = new VBox(20);
        playerSelectionBox.setAlignment(Pos.CENTER);

        Button[] playerButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            int playerCount = i + 2;
            Button btn = createStyledButton(playerCount + " joueurs");
            btn.setOnAction(e -> showPlayerChoice(stage, playerCount));
            playerButtons[i] = btn;
        }

        playerSelectionBox.getChildren().add(label);
        playerSelectionBox.getChildren().addAll(playerButtons);

        StackPane root = new StackPane(playerSelectionBox);
        root.setBackground(createBackground());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        return scene;
    }

    // Méthode utilitaire pour créer un bouton avec style uniforme
    private static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(300);
        button.setStyle(buttonStyle());
        return button;
    }

    // Affiche le choix pour définir IA ou humain
    private static void showPlayerChoice(Stage stage, int numPlayers) {
        VBox aiChoiceBox = new VBox(20);
        aiChoiceBox.setAlignment(Pos.CENTER);

        Label label = new Label("Choisissez qui sera l'IA (max 1) — joueurs: " + numPlayers);
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Button aiButton = createStyledButton("Choisir IA");
        aiButton.setOnAction(e -> System.out.println("IA sélectionnée"));

        Button humanButton = createStyledButton("Choisir Humain");
        humanButton.setOnAction(e -> showTerrainChoice(stage));

        aiChoiceBox.getChildren().addAll(label, aiButton, humanButton);

        StackPane root = new StackPane(aiChoiceBox);
        root.setBackground(createBackground());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    // Affiche le choix du terrain
    private static void showTerrainChoice(Stage stage) {
        VBox terrainBox = new VBox(20);
        terrainBox.setAlignment(Pos.CENTER);

        Label label = new Label("Choisissez le terrain");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Utilisation correcte des références de méthodes statiques du modèle Plateau
        Button cityButton = createTerrainButton(stage, "Ville", Plateau::generateCityTerrain);
        Button islandButton = createTerrainButton(stage, "Île", Plateau::generateIsland);

        cityButton.setPrefWidth(300);
        islandButton.setPrefWidth(300);

        terrainBox.getChildren().addAll(label, cityButton, islandButton);

        StackPane root = new StackPane(terrainBox);
        root.setBackground(createBackground());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    // Interface fonctionnelle pour générer terrain
    @FunctionalInterface
    private interface TerrainGenerator {
        void generate(Plateau plateau);
    }

    // Création d’un bouton pour choix de terrain, avec action associée
    private static Button createTerrainButton(Stage stage, String name, TerrainGenerator generator) {
        Button button = new Button(name);
        button.setStyle(buttonStyle());
        button.setOnAction(e -> {
            System.out.println("Terrain sélectionné : " + name);

            Plateau plateau = new Plateau();
            generator.generate(plateau);

            GameController controller = new GameController(plateau);

            GameControllerPane controllerPane = new GameControllerPane(controller);

            BorderPane root = new BorderPane();
            root.setCenter(plateau);
            root.setRight(controllerPane);

            Scene scene = new Scene(root, 1920, 1080);
            stage.setScene(scene);
            stage.setFullScreen(true);
        });
        return button;
    }

    // Méthode pour créer un arrière-plan (background)
    private static Background createBackground() {
        URL bgUrl = GameMenu.class.getResource("/images/war_background.jpg");
        if (bgUrl == null) {
            System.err.println("Image de fond introuvable !");
            return new Background(new BackgroundFill(javafx.scene.paint.Color.DARKSLATEGRAY, null, null));
        }

        return new Background(
                new BackgroundImage(
                        new Image(bgUrl.toExternalForm(), -1, -1, true, true),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, true, false)
                )
        );
    }

    // Style CSS uniforme pour tous les boutons
    private static String buttonStyle() {
        return "-fx-font-size: 24px; -fx-background-color: #444; -fx-text-fill: white;";
    }
}

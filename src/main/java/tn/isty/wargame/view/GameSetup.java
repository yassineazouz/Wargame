package tn.isty.wargame.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.isty.wargame.controller.GameController;
import tn.isty.wargame.model.*;
import tn.isty.wargame.util.SaveManager;

import java.util.ArrayList;
import java.util.List;

public class GameSetup {

    public static Scene createSetupScene(Stage stage) {
        Label label = new Label("Choisissez le nombre de joueurs");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        VBox playerSelectionBox = new VBox(20);
        playerSelectionBox.setAlignment(Pos.CENTER);

        Button[] playerButtons = new Button[3];  // 2 à 4 joueurs

        for (int i = 0; i < 3; i++) {
            int playerCount = i + 2;
            Button btn = new Button(playerCount + " joueurs");
            btn.setStyle("-fx-font-size: 18px; -fx-background-color: #444; -fx-text-fill: white;");
            btn.setOnAction(e -> showPlayerChoice(stage, playerCount));
            playerButtons[i] = btn;
        }

        playerSelectionBox.getChildren().add(label);
        playerSelectionBox.getChildren().addAll(playerButtons);

        StackPane root = new StackPane();
        root.setBackground(getBackgroundImage());
        root.getChildren().add(playerSelectionBox);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);

        return scene;
    }

    private static void showPlayerChoice(Stage stage, int numPlayers) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label label = new Label("Choisissez qui sera IA (max 1)");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        List<Boolean> iaFlags = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            iaFlags.add(false); // par défaut humain
        }

        VBox buttonBox = new VBox(10);
        for (int i = 0; i < numPlayers; i++) {
            int index = i;
            Button toggleBtn = new Button("Joueur " + (i + 1) + " : Humain");
            toggleBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #444; -fx-text-fill: white;");
            toggleBtn.setOnAction(e -> {
                boolean isIA = !iaFlags.get(index);
                iaFlags.set(index, isIA);
                toggleBtn.setText("Joueur " + (index + 1) + " : " + (isIA ? "IA" : "Humain"));
            });
            buttonBox.getChildren().add(toggleBtn);
        }

        Button startGame = new Button("Commencer la partie");
        startGame.setStyle("-fx-font-size: 18px; -fx-background-color: #222; -fx-text-fill: white;");
        startGame.setOnAction(e -> {
            List<Player> players = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                players.add(new Player("Joueur " + (i + 1), iaFlags.get(i)));
            }

            Plateau plateau = new Plateau(10, 10);
            GameState gameState = new GameState(players, plateau);
            gameState.initializeGame();
            HexagonTile.setSharedGameState(gameState);

            if (players.size() >= 2) {
                Player p1 = players.get(0);
                Player p2 = players.get(1);

                Unit archer = new Unit("A", "archer", 20, 5, 2, 3, 3, p1);
                Unit tank = new Unit("T", "tank", 30, 4, 6, 2, 2, p2);

                p1.addUnit(archer);
                p2.addUnit(tank);

                plateau.placerUnite(2, 2, archer);
                plateau.placerUnite(5, 5, tank);
            }

            GameController controller = new GameController(gameState);

            // 🔘 Bouton Fin de tour
            Button endTurnButton = new Button("Fin de tour");
            endTurnButton.setLayoutX(20);
            endTurnButton.setLayoutY(20);
            endTurnButton.setStyle("-fx-font-size: 16px;");
            endTurnButton.setOnAction(ev -> controller.endTurn());

            // 💾 Bouton Sauvegarder
            Button saveButton = new Button("Sauvegarder");
            saveButton.setLayoutX(140);
            saveButton.setLayoutY(20);
            saveButton.setStyle("-fx-font-size: 16px;");
            saveButton.setOnAction(ev -> SaveManager.sauvegarder(gameState, "savegame.ser"));

            Button returnButton = new Button("Retour Menu");
            returnButton.setLayoutX(280);
            returnButton.setLayoutY(20);
            returnButton.setStyle("-fx-font-size: 16px;");
            returnButton.setOnAction(ev -> GameMenu.createMenuScene(stage));

            plateau.getChildren().addAll(endTurnButton, saveButton, returnButton);

            Scene gameScene = new Scene(plateau, 1280, 800);
            stage.setScene(gameScene);
            stage.setTitle("Wargame - Partie");
        });

        layout.getChildren().addAll(label, buttonBox, startGame);

        StackPane root = new StackPane();
        root.setBackground(getBackgroundImage());
        root.getChildren().add(layout);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
    }

    private static Background getBackgroundImage() {
        return new Background(new BackgroundImage(
                new Image(GameMenu.class.getResource("/images/war_background.jpg").toExternalForm(),
                        -1, -1, true, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        ));
    }
}

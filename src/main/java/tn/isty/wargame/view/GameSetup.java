package tn.isty.wargame.view;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.isty.wargame.controller.GameController;
import tn.isty.wargame.model.GameState;
import tn.isty.wargame.model.Plateau;
import tn.isty.wargame.model.Player;
import javafx.scene.paint.Color;

public class GameSetup {

    public static Scene createSetupScene(Stage stage) {
        // Créer un label pour la sélection du nombre de joueurs
        Label label = new Label("Choisissez le nombre de joueurs");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Créer un layout pour les boutons de sélection du nombre de joueurs
        VBox playerSelectionBox = new VBox(20);
        playerSelectionBox.setAlignment(Pos.CENTER);

        // Créer les boutons pour choisir le nombre de joueurs
        Button[] playerButtons = new Button[4];  // Nombre maximum de joueurs

        for (int i = 0; i < 4; i++) {
            int playerCount = i + 2;  // Nombre de joueurs (2, 3, 4, 5)
            playerButtons[i] = new Button(playerCount + " joueurs");
            playerButtons[i].setStyle("-fx-font-size: 18px; -fx-background-color: #444; -fx-text-fill: white;");
            playerButtons[i].setOnAction(e -> {
                // Afficher les boutons pour choisir IA ou Humain
                showPlayerChoice(stage, playerCount);
            });
        }

        playerSelectionBox.getChildren().addAll(label);
        playerSelectionBox.getChildren().addAll(playerButtons);

        // Fond d'écran étendu
        BackgroundImage bgImage = new BackgroundImage(
                new Image(GameMenu.class.getResource("/images/war_background.jpg").toExternalForm(),
                        -1, -1,
                        true, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        StackPane root = new StackPane();
        root.setBackground(new Background(bgImage));  // Appliquer le fond d'écran
        root.getChildren().add(playerSelectionBox);

        // Créer la scène en plein écran
        Scene scene = new Scene(root);
        stage.setMaximized(true);  // Maximiser la fenêtre
        stage.setScene(scene);

        stage.setFullScreen(true);

        return scene;
    }

    private static void showPlayerChoice(Stage stage, int numPlayers) {
        VBox aiChoiceBox = new VBox(20);
        aiChoiceBox.setAlignment(Pos.CENTER);

        Label label = new Label("Choisissez qui sera l'IA (max 1)");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Button aiButton = new Button("Choisir IA");
        aiButton.setStyle("-fx-font-size: 18px; -fx-background-color: #444; -fx-text-fill: white;");
        aiButton.setOnAction(e -> {
            // Pour l’instant, simple affichage
            System.out.println("Choisir IA...");
        });

        Button humanButton = new Button("Choisir Humain");
        humanButton.setStyle("-fx-font-size: 18px; -fx-background-color: #444; -fx-text-fill: white;");
        humanButton.setOnAction(e -> {
            // Pour l’instant, simple affichage
            System.out.println("Choisir Humain...");
        });

        Button startGame = new Button("Commencer la partie");
        startGame.setStyle("-fx-font-size: 18px; -fx-background-color: #222; -fx-text-fill: white;");
        startGame.setOnAction(ev -> {
            // ⚠️ À améliorer plus tard : pour l’instant 1 humain + 1 IA
            Player p1 = new Player("Joueur 1", false);
            Player p2 = new Player("Joueur 2", true);

            Plateau plateau = new Plateau(5, 5); // Taille par défaut
            GameState gameState = new GameState(List.of(p1, p2), plateau);
            GameController controller = new GameController(gameState);

            GameBoard board = new GameBoard(); // Tu peux plus tard passer gameState ici
            Scene gameScene = new Scene(board, 1280, 800);

            stage.setScene(gameScene);
            stage.setTitle("Wargame - Plateau");
        });

        aiChoiceBox.getChildren().addAll(label, aiButton, humanButton, startGame);

        // Fond d'écran
        BackgroundImage bgImage = new BackgroundImage(
                new Image(GameMenu.class.getResource("/images/war_background.jpg").toExternalForm(),
                        -1, -1,
                        true, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        StackPane root = new StackPane();
        root.setBackground(new Background(bgImage));
        root.getChildren().add(aiChoiceBox);

        Scene scene = new Scene(root);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

}





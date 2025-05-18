package tn.isty.wargame.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameMenu {

    public static Scene createMenuScene(Stage stage) {
        // Titre
        Label title = new Label("WARGAME");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 80));

        // Boutons
        Button startButton = new Button("Nouvelle Partie");
        Button sauveButton = new Button("charger");
        Button quitButton = new Button("Quitter");

        startButton.setPrefWidth(300);
        sauveButton.setPrefWidth(300);
        quitButton.setPrefWidth(300);

        startButton.setStyle("-fx-font-size: 24px; -fx-background-color: #444; -fx-text-fill: white;");
        quitButton.setStyle("-fx-font-size: 24px; -fx-background-color: #222; -fx-text-fill: white;");
        sauveButton.setStyle("-fx-font-size: 24px; -fx-background-color: #444; -fx-text-fill: white;");

        startButton.setOnAction(e -> {
            // Lancer la scène de configuration en passant uniquement le stage
            GameSetup.createSetupScene(stage);
        });

        quitButton.setOnAction(e -> stage.close());

        VBox menuBox = new VBox(30, title, startButton,sauveButton,quitButton);
        menuBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(menuBox);

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
        root.setBackground(new Background(bgImage));

        // Créer la scène en plein écran
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);  // Passer en plein écran

        return scene;
    }
}

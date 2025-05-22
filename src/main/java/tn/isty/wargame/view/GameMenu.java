package tn.isty.wargame.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.URL;

public class GameMenu {
    private static final Logger LOGGER = Logger.getLogger(GameMenu.class.getName());


    public static Scene createMenuScene(Stage stage) {
        VBox menuBox = createMenuBox(stage);
        StackPane root = new StackPane(menuBox);

        Background background = createBackground();
        if (background != null) {
            root.setBackground(background);
        } else {
            root.setStyle("-fx-background-color: black;"); // Fond de secours si image non trouvée
        }

        return new Scene(root, 1920, 1080);
    }

    private static VBox createMenuBox(Stage stage) {


        Button startButton = new Button("Nouvelle Partie");
        Button loadButton = new Button("Charger");
        Button quitButton = new Button("Quitter");

        startButton.setPrefWidth(300);
        loadButton.setPrefWidth(300);
        quitButton.setPrefWidth(300);

        String buttonStyle = "-fx-font-size: 24px; -fx-background-color: #444; -fx-text-fill: white;";
        startButton.setStyle(buttonStyle);
        loadButton.setStyle(buttonStyle);
        quitButton.setStyle("-fx-font-size: 24px; -fx-background-color: #444; -fx-text-fill: white;");

        // Actions
        startButton.setOnAction(e -> stage.setScene(GameSetup.createSetupScene(stage)));
        loadButton.setOnAction(e -> System.out.println("Fonction Charger non encore implémentée."));
        quitButton.setOnAction(e -> stage.close());

        VBox menuBox = new VBox(30, startButton, loadButton, quitButton);
        menuBox.setAlignment(Pos.CENTER);

        return menuBox;
    }

    private static Background createBackground() {
        try {
            URL imageUrl = GameMenu.class.getResource("/images/war_background.jpg");
            if (imageUrl == null) {
                System.err.println("Image de fond non trouvée !");
                return null;
            }

            Image backgroundImage = new Image(imageUrl.toExternalForm(),
                    -1, -1, true, true);
            return new Background(
                    new BackgroundImage(
                            backgroundImage,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(100, 100, true, true, true, false)
                    )
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de l'image de fond", e);

            return null;
        }
    }
}

package tn.isty.wargame;
import tn.isty.wargame.view.GameBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Wargame - Plateau Test");

        GameBoard board = new GameBoard(); // Ton plateau plein de plaines
        StackPane root = new StackPane(board); // Pour centrer le plateau

        Scene scene = new Scene(root); // Ne pas définir de taille pour fullscreen

        stage.setScene(scene);
        stage.setFullScreen(true); // Plein écran
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package tn.isty.wargame;

import javafx.application.Application;
import javafx.stage.Stage;
import tn.isty.wargame.controller.GameController;
import tn.isty.wargame.model.GameState;
import tn.isty.wargame.model.Plateau;
import tn.isty.wargame.model.Player;
import tn.isty.wargame.model.Unit;

import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Plateau plateau = new Plateau(5, 5);
        Player p1 = new Player("Ghassen", false);
        Player p2 = new Player("Oussama", true);
        GameState gameState = new GameState(List.of(p1, p2), plateau);

        gameState.initializeGame();
        plateau.afficherConsole();
        GameController controller = new GameController(gameState);

        controller.startGame();

        

        //stage.setTitle("Wargame - Plateau Test");

        //GameBoard board = new GameBoard(); // Ton plateau plein de plaines
        //StackPane root = new StackPane(board); // Pour centrer le plateau

        //Scene scene = new Scene(root); // Ne pas définir de taille pour fullscreen

        //stage.setScene(scene);
        //stage.setFullScreen(true); // Plein écran
        //stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

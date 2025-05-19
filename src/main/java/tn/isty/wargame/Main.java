package tn.isty.wargame;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.isty.wargame.controller.GameController;
import tn.isty.wargame.model.GameState;
import tn.isty.wargame.model.HexagonTile;
import tn.isty.wargame.model.Plateau;
import tn.isty.wargame.model.Player;
import tn.isty.wargame.model.Unit;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // Crée les joueurs
        Player p1 = new Player("Ghassen", false);
        Player p2 = new Player("Oussama", false);

        // Plateau
        Plateau plateau = new Plateau(10, 10);
        GameState gameState = new GameState(List.of(p1, p2), plateau);

        // 🔥 LIGNE IMPORTANTE
        HexagonTile.setSharedGameState(gameState);

        // Création d’unités
        Unit archer = new Unit("A", "archer", 20, 5, 2, 3, 3, p1);
        Unit cav = new Unit("C", "cavalerie", 25, 6, 3, 5, 3, p1);
        Unit mage = new Unit("M", "mage", 15, 8, 1, 3, 4, p1);
        Unit tank = new Unit("T", "infanterie_lourde", 30, 4, 6, 2, 2, p2);
        Unit infantry = new Unit("B", "infanterie", 20, 5, 3, 3, 3, p2);

        // Ajout au joueur
        p1.addUnit(archer);
        p1.addUnit(cav);
        p1.addUnit(mage);
        p2.addUnit(tank);
        p2.addUnit(infantry);

        // Placement sur le plateau
        plateau.placerUnite(2, 2, archer);
        plateau.placerUnite(3, 3, cav);
        plateau.placerUnite(2, 4, mage);
        plateau.placerUnite(5, 3, tank);
        plateau.placerUnite(4, 2, infantry);

        // Affichage console
        plateau.afficherConsole();

        // Contrôleur (même si non encore utilisé ici)
        GameController controller = new GameController(gameState);

        // Scène JavaFX
        Scene scene = new Scene(plateau, 1200, 800);
        stage.setTitle("Wargame - Plateau Graphique");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package tn.isty.wargame;

import javafx.application.Application;
import javafx.stage.Stage;
import tn.isty.wargame.controller.GameController;
import tn.isty.wargame.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Bienvenue dans Wargame ===");

        // Taille du plateau
        System.out.print("Entrez le nombre de lignes du plateau : ");
        int rows = Integer.parseInt(scanner.nextLine());
        System.out.print("Entrez le nombre de colonnes du plateau : ");
        int cols = Integer.parseInt(scanner.nextLine());
        Plateau plateau = new Plateau(rows, cols);

        // Nombre de joueurs
        System.out.print("Combien de joueurs (2 à 4) ? : ");
        int nbJoueurs = Integer.parseInt(scanner.nextLine());
        while (nbJoueurs < 2 || nbJoueurs > 4) {
            System.out.print("Veuillez entrer un nombre entre 2 et 4 : ");
            nbJoueurs = Integer.parseInt(scanner.nextLine());
        }

        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= nbJoueurs; i++) {
            System.out.print("Le joueur " + i + " est-il une IA ? (o/n) : ");
            String isAi = scanner.nextLine();
            System.out.print("Nom du joueur " + i + " : ");
            String nom = scanner.nextLine();
            players.add(new Player(nom, isAi.equalsIgnoreCase("o")));
        }

        GameState gameState = new GameState(players, plateau);

        // Placement initial des unités pour chaque joueur
        for (int idx = 0; idx < players.size(); idx++) {
            Player p = players.get(idx);
            for (int i = 0; i < 2; i++) {
                Unit u = new Unit(p.getName() + "-U" + (i + 1), "infanterie", 20, 5, 2, 3, 3, p);
                int row = (idx % 2 == 0) ? idx : rows - 1 - idx;
                int col = (i == 0) ? 1 + idx : cols - 2 - idx;
                plateau.placerUnite(row, col, u);
                p.addUnit(u);
            }
        }

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

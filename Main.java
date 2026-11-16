package Fx ;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Fx.Player;

public class Main extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Player player = new Player(40, 200);

        GameView gameView = new GameView(root);

        new GameController(player, gameView, scene);

        stage.setTitle("Super Mario Ball - Multi-Levels");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    	}
    
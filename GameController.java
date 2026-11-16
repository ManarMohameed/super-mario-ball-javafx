package Fx;

import Fx.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    private Player player;
    private GameView view;

    private Set<KeyCode> keysPressed = new HashSet<>();

    private double cameraX = 0;
    private AnimationTimer timer;

    private enum State { MENU, PLAYING, LEVEL_CLEAR, GAME_OVER }
    private State gameState = State.MENU;

    private int currentLevel = 1;

    private double enemy1X = 500;
    private double enemy1Speed = 2;

    private double enemy2X = 1300;
    private double enemy2Speed = 4;

    private double[] coinX = {300, 700, 1200, 1700, 2200};
    private double[] coinY = {250, 250, 250, 250, 250};
    private boolean[] collectedCoins = {false, false, false, false, false};
    private int totalCoins = 5;
    private int collectedCount = 0;

    private static final double SCREEN_WIDTH = Main.WIDTH;

    public GameController(Player player, GameView view, Scene scene) {
        this.player = player;
        this.view = view;
        view.drawLevel(currentLevel);

        scene.setOnKeyPressed(e -> {
            if (gameState == State.PLAYING)
                keysPressed.add(e.getCode());
        });

        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
 
        
        view.startButton.setOnAction(e -> {
            view.menuLayout.setVisible(false);
            gameState = State.PLAYING;
        });

        // بدء اللعبة
        view.restartButton.setOnAction(e -> {
            resetGame();
            gameState = State.PLAYING;
        });

        // الانتقال للمرحله الثانبة 
        view.nextLevelButton.setOnAction(e -> {
            view.winLayout.setVisible(false);
            if (currentLevel == 1) {
                currentLevel = 2;
                player.setX(40);
                player.setY(200);
                resetLevelObjects();
                enemy1Speed = 4;
            } else {
                currentLevel = 1;
                resetGame();
                view.menuLayout.setVisible(true);
                gameState = State.MENU;
                return;
            }

            view.drawLevel(currentLevel);
            gameState = State.PLAYING;
        });

        startGameLoop();
    }

    private void startGameLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameState == State.PLAYING) {
                    updateGame();
                }
            }
        };
        timer.start();
    }

    private void updateGame() {
        // حركة اللاعب
        if (keysPressed.contains(KeyCode.RIGHT)) player.moveRight();
        else if (keysPressed.contains(KeyCode.LEFT)) player.moveLeft();
        else player.stop();
        if (keysPressed.contains(KeyCode.SPACE) || keysPressed.contains(KeyCode.UP))
            player.jump();
        player.update();
        // الحفرة
        checkPit();
        if (player.isDead()) {
            gameState = State.GAME_OVER;
            view.gameOverLayout.setVisible(true);
            return;
        }
        checkCoins();
        updateEnemies();
        checkEnemyCollision();
        checkWin();
        updateCamera();

        view.updateUI(player,cameraX,enemy1X,enemy2X,currentLevel,currentLevel == 2,collectedCoins,coinX,coinY);
    }

    
    private void checkPit() {
        if (currentLevel != 2) return;
        if (player.getX() > 800 &&
            player.getX() < 950 &&
            player.getY() >= 340) {
            player.startFallingIntoPit();
        }
    }

    private void checkCoins() {
        for (int i = 0; i < totalCoins; i++) {
            if (!collectedCoins[i]) {
                double dx = player.getX() - coinX[i];
                double dy = player.getY() - coinY[i];
                if (Math.abs(dx) < 25 && Math.abs(dy) < 25) {
                    collectedCoins[i] = true;
                    collectedCount++;
                    player.addScore(100);
                }
            }
        }
    }
    
    private void updateEnemies() {
        enemy1X += enemy1Speed;
        if (enemy1X > 700 || enemy1X < 350)
            enemy1Speed *= -1;
        if (currentLevel == 2) {
            enemy2X += enemy2Speed;
            if (enemy2X > 1600 || enemy2X < 1100)
                enemy2Speed *= -1;
        }
    }

    
    private void checkEnemyCollision() {
        if (checkCollision(player.getX(), player.getY(), enemy1X, 325))
            handleHit();
        if (currentLevel == 2 &&
            checkCollision(player.getX(), player.getY(), enemy2X, 325))
            handleHit();
    }

    
    private void checkWin() {
        if (player.getX() >= 2400 ) {
            gameState = State.LEVEL_CLEAR;
            if (currentLevel == 1)
                view.nextLevelButton.setText("PROCEED TO LEVEL 2");
            else
                view.nextLevelButton.setText("YOU WIN! RETURN TO MENU");
            view.winLayout.setVisible(true);
        }
    }


    private void updateCamera() {
        cameraX = player.getX() - SCREEN_WIDTH / 2;
        if (cameraX < 0) cameraX = 0;
        double maxCameraX = 2500 - SCREEN_WIDTH;
        if (cameraX > maxCameraX)
            cameraX = maxCameraX;
    }

    
    private void handleHit() {
        player.loseLife();
        keysPressed.clear();
        if (player.getLives() <= 0) {
            gameState = State.GAME_OVER;
            view.gameOverLayout.setVisible(true);
        }
    }

    
    private void resetGame() {
        currentLevel = 1;
        player.reset();
        resetLevelObjects();
        view.drawLevel(currentLevel);
        view.gameOverLayout.setVisible(false);
    }

  
    private void resetLevelObjects() {
        cameraX = 0;
        enemy1X = 500;
        enemy2X = 1300;
        collectedCount = 0;
        for (int i = 0; i < collectedCoins.length; i++)
            collectedCoins[i] = false;
    }


    private boolean checkCollision(double pX, double pY, double eX, double eY) {
        return Math.abs(pX - (eX + 15)) < 25 &&
               Math.abs(pY - (eY + 15)) < 25;
    }
}
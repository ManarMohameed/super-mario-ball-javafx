package Fx;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import Fx.Player;

import javafx.scene.shape.Polygon;
import java.util.Random;

public class GameView {

    protected Pane root;
    protected Circle mario;
    private Canvas canvas; 
    private GraphicsContext gc;

    protected Rectangle enemy1;
    protected Rectangle enemy2;
    
    protected VBox menuLayout;
    protected VBox gameOverLayout;
    protected VBox winLayout; 
    
    protected Button startButton;
    protected Button restartButton;
    protected Button nextLevelButton;
    
    protected Circle[] coins;
    private javafx.scene.Node[] hearts;
    private Circle coinIcon;
    private Text scoreText;
    private Text levelText;
    
    private static final int MAP_WIDTH = Main.WIDTH * 10;
    private static final int GROUND_Y = 370;

    public GameView(Pane root) {
        this.root = root;
        
        canvas = new Canvas(MAP_WIDTH, Main.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // إضافة مربعات الأعداء
        enemy1 = new Rectangle(0, 325, 30, 30); enemy1.setFill(Color.DARKRED);
        enemy2 = new Rectangle(0, 325, 30, 30); enemy2.setFill(Color.PURPLE); 
        root.getChildren().addAll(enemy1, enemy2);

        coinIcon = new Circle(30, 30, 8);
        coinIcon.setFill(Color.GOLD);
        root.getChildren().add(coinIcon);
        scoreText = new Text(45, 35, "0");
        scoreText.setFill(Color.WHITE);
        scoreText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(scoreText);

        levelText = new Text();
        levelText.setFill(Color.WHITE);
        levelText.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        levelText.setX(Main.WIDTH / 2 - 60);
        levelText.setY(80);
        root.getChildren().add(levelText);
        
        // الكورة الحمراء (اللاعب)
        mario = new Circle(15, Color.RED);
        root.getChildren().add(mario);
        
        createHearts();
        coins = new Circle[5];
        for (int i = 0; i < coins.length; i++) {
            coins[i] = new Circle(10);
            coins[i].setFill(Color.GOLD);
            root.getChildren().add(coins[i]);
        }

        createUIOverlays(); }
        
    private void createHearts() {
        hearts = new Pane[3];
        for (int i = 0; i < hearts.length; i++) {
            double x = 700 + (i * 25);
            double y = 20;
            Circle left = new Circle(x, y, 5);
            left.setFill(Color.RED);
            Circle right = new Circle(x + 8, y, 5);
            right.setFill(Color.RED);
            Polygon bottom = new Polygon();
            bottom.getPoints().addAll(
                 x - 5, y,
                 x + 13, y,
                 x + 4, y + 14
            );
            bottom.setFill(Color.RED);
            Pane heart = new Pane();
            heart.getChildren().addAll(
                left,
                right,
                bottom
            );
            hearts[i] = heart;
            root.getChildren().add(heart);
        }
    }

    // الرسم 
    public void drawLevel(int level) {
        gc.clearRect(0, 0, MAP_WIDTH, Main.HEIGHT);

        for (int y = 0; y < Main.HEIGHT; y++) {
            double ratio = (double) y / Main.HEIGHT;
            if (level == 1) {
                int r, g, b;
                if (ratio < 0.35) { double t = ratio / 0.35; r = (int)(80 + t * 70); g = (int)(50 + t * 40); b = (int)(140 + t * 30); }
                else if (ratio < 0.55) { double t = (ratio - 0.35) / 0.2; r = (int)(150 + t * 90); g = (int)(90 - t * 30); b = (int)(170 - t * 70); }
                else if (ratio < 0.75) { double t = (ratio - 0.55) / 0.2; r = (int)(240); g = (int)(60 + t * 80); b = (int)(100 - t * 90); }
                else { double t = (ratio - 0.75) / 0.25; r = (int)(240 + t * 15); g = (int)(140 + t * 80); b = (int)(10); }
                gc.setFill(Color.rgb(Math.min(255,r), Math.max(0,g), Math.max(0,b)));
            } else {
                int r = (int)(20 + ratio * 30); int g = (int)(10 + ratio * 20); int b = (int)(50 + ratio * 40);
                gc.setFill(Color.rgb(r, g, b));
            }
            gc.fillRect(0, y, MAP_WIDTH, 1);
        }
 
// لنجوم
        Random randS = new Random(55);
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 200; i++) {
            int x = randS.nextInt(MAP_WIDTH); int y = randS.nextInt(150); int size = randS.nextInt(2) + 1;
            gc.fillRect(x, y, size, size);
        }

// لغيوم
        Random randC = new Random(33);
        for (int i = 0; i < 15; i++) {
            int cx = randC.nextInt(MAP_WIDTH); int cy = randC.nextInt(100) + 20; drawCloud(gc, cx, cy);
        }

        //  الاشجار
        Random randT = new Random(66);
        for (int x = 0; x < MAP_WIDTH; x += randT.nextInt(120) + 60) {
            // حماية منطقة الحفرة في لفل 2 لكي لا تطفو الأشجار في الهواء
            if (level == 2 && x > 750 && x < 950) continue;
            drawBush(gc, x, GROUND_Y - 55, randT);
        }

// الاشجار
        if (level == 1) {
            //  لفل 1
            gc.setFill(Color.web("#2D6A1F")); gc.fillRect(0, GROUND_Y + 5, MAP_WIDTH, Main.HEIGHT);
            gc.setFill(Color.web("#3A8C28")); gc.fillRect(0, GROUND_Y, MAP_WIDTH, 10);
        } else {
            // الأرض المكسورة  لـ لفل 2
            gc.setFill(Color.web("#2D6A1F"));
            gc.fillRect(0, GROUND_Y + 5, 800, Main.HEIGHT);
            gc.setFill(Color.web("#3A8C28")); gc.fillRect(0, GROUND_Y, 800, 10);
            
            gc.setFill(Color.web("#2D6A1F"));
            gc.fillRect(950, GROUND_Y + 5, MAP_WIDTH - 950, Main.HEIGHT);
            gc.setFill(Color.web("#3A8C28")); gc.fillRect(950, GROUND_Y, MAP_WIDTH - 950, 10);
            
            // الوحه
            gc.setFill(Color.RED); gc.fillRect(750, GROUND_Y - 30, 20, 20);
            gc.setFill(Color.WHITE); gc.fillRect(758, GROUND_Y - 25, 4, 10);
        }

        // الاعشاب
        Random rand = new Random(42);
        for (int x = 2; x < MAP_WIDTH; x += 5) {
            if (level == 2 && x > 800 && x < 950) continue; // تخطي منطقة الفراغ (الحفرة)
            int h = rand.nextInt(6) + 4; int shade = rand.nextInt(3);
            if (shade == 0) gc.setFill(Color.web("#4CAF28"));
            else if (shade == 1) gc.setFill(Color.web("#2E7D1A"));
            else gc.setFill(Color.web("#66BB2A"));
            gc.fillRect(x, GROUND_Y - h, 2, h);
            if (rand.nextBoolean()) {
                gc.fillRect(x - 1, GROUND_Y - h + 2, 1, h - 2); gc.fillRect(x + 2, GROUND_Y - h + 2, 1, h - 2);
            }
        }

        // رسم الورود الملونة الأصلية الخاصة بكِ
        Random randF = new Random(77);
        for (int x = 10; x < MAP_WIDTH; x += randF.nextInt(80) + 40) {
            if (level == 2 && x > 800 && x < 950) continue; // تخطي الحفرة
            drawFlower(gc, x, GROUND_Y - 12, randF);
        }

        // رسم العلم
        double flagX = 2420;
        double flagBaseY = GROUND_Y;
        gc.setFill(Color.GRAY); gc.fillRect(flagX - 10, flagBaseY - 20, 30, 20);
        gc.setFill(Color.DARKGRAY); gc.strokeRect(flagX - 10, flagBaseY - 20, 30, 20);
        
        gc.setFill(Color.LIGHTGREEN); gc.fillRect(flagX, flagBaseY - 170, 10, 150);
        gc.setFill(Color.GREEN); gc.strokeRect(flagX, flagBaseY - 170, 10, 150);
        
        gc.setFill(Color.GOLD); gc.fillOval(flagX - 5, flagBaseY - 185, 20, 20);
        
        gc.setFill(Color.WHITE); gc.fillRect(flagX - 40, flagBaseY - 160, 40, 30);
        gc.setStroke(Color.BLACK); gc.strokeRect(flagX - 40, flagBaseY - 160, 40, 30);
        gc.setFill(Color.RED); gc.fillOval(flagX - 25, flagBaseY - 150, 12, 12);
    }


    private void drawCloud(GraphicsContext gc, int x, int y) {
        gc.setFill(Color.rgb(220, 220, 240)); gc.fillRect(x, y, 40, 12); gc.fillRect(x + 8, y - 8, 24, 12);
        gc.setFill(Color.WHITE); gc.fillRect(x + 2, y + 2, 36, 8); gc.fillRect(x + 10, y - 6, 20, 10);
    }

    private void drawBush(GraphicsContext gc, int x, int y, Random rand) {
        gc.setFill(Color.web("#8B4513")); gc.fillRect(x + 18, y + 30, 14, 25);
        gc.setFill(Color.web("#6B3010")); gc.fillRect(x + 25, y + 30, 7, 25);
        gc.setFill(Color.web("#1A5C10")); gc.fillRect(x, y + 10, 50, 20);
        gc.setFill(Color.web("#228B22")); gc.fillRect(x + 5, y, 40, 20);
        gc.setFill(Color.web("#2ECC40")); gc.fillRect(x + 10, y - 8, 30, 15);
        gc.setFill(Color.web("#55DD55"));
        for (int i = 0; i < 5; i++) { gc.fillRect(x + rand.nextInt(40) + 5, y + rand.nextInt(20), 3, 3); }
    }

    private void drawFlower(GraphicsContext gc, int x, int y, Random rand) {
        gc.setFill(Color.web("#228B22")); gc.fillRect(x, y, 2, 8);
        int colorType = rand.nextInt(4); Color petalColor;
        if (colorType == 0) petalColor = Color.PINK;
        else if (colorType == 1) petalColor = Color.WHITE;
        else if (colorType == 2) petalColor = Color.VIOLET;
        else petalColor = Color.HOTPINK;
        gc.setFill(petalColor); gc.fillOval(x - 3, y - 4, 4, 4); gc.fillOval(x + 3, y - 4, 4, 4);
        gc.fillOval(x - 1, y - 7, 4, 4); gc.fillOval(x - 1, y - 1, 4, 4);
        gc.setFill(Color.YELLOW); gc.fillOval(x - 1, y - 4, 4, 4);
    }

    // رس الواجهةة
    private void createUIOverlays() {
        menuLayout = new VBox(20); menuLayout.setAlignment(Pos.CENTER); menuLayout.setPrefSize(Main.WIDTH, Main.HEIGHT);
        menuLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
        Text menuTitle = new Text("SUPER MARIO BALL"); menuTitle.setFont(Font.font("Arial", 40)); menuTitle.setFill(Color.GOLD);
        startButton = new Button("START GAME"); startButton.setStyle("-fx-font-size: 18px; -fx-background-color: #3A8C28; -fx-text-fill: white; -fx-font-weight: bold;");
        menuLayout.getChildren().addAll(menuTitle, startButton); root.getChildren().add(menuLayout);

        gameOverLayout = new VBox(20); gameOverLayout.setAlignment(Pos.CENTER); gameOverLayout.setPrefSize(Main.WIDTH, Main.HEIGHT);
        gameOverLayout.setStyle("-fx-background-color: rgba(150, 0, 0, 0.85);"); gameOverLayout.setVisible(false);
        Text gameOverTitle = new Text("GAME OVER"); gameOverTitle.setFont(Font.font("Arial", 50)); gameOverTitle.setFill(Color.WHITE);
        restartButton = new Button("TRY AGAIN"); restartButton.setStyle("-fx-font-size: 18px; -fx-text-fill: darkred; -fx-font-weight: bold;");
        gameOverLayout.getChildren().addAll(gameOverTitle, restartButton); root.getChildren().add(gameOverLayout);

        winLayout = new VBox(20); winLayout.setAlignment(Pos.CENTER); winLayout.setPrefSize(Main.WIDTH, Main.HEIGHT);
        winLayout.setStyle("-fx-background-color: rgba(0, 100, 200, 0.9);"); winLayout.setVisible(false);
        Text winTitle = new Text("LEVEL CLEARED!"); winTitle.setFont(Font.font("Arial", 45)); winTitle.setFill(Color.WHITE);
        nextLevelButton = new Button("PROCEED"); nextLevelButton.setStyle("-fx-font-size: 18px; -fx-background-color: gold; -fx-font-weight: bold;");
        winLayout.getChildren().addAll(winTitle, nextLevelButton); root.getChildren().add(winLayout);
    }

    
    public void updateUI(Player p,double cameraX,double e1X,double e2X,int currentLevel,boolean showEnemy2,boolean[] collectedCoins,double[] coinX,double[] coinY) {
        mario.setCenterX(p.getX() - cameraX);
        mario.setCenterY(p.getY());
        
        enemy1.setX(e1X - cameraX);
        if (showEnemy2) {
            enemy2.setVisible(true); enemy2.setX(e2X - cameraX);
        } else {
            enemy2.setVisible(false);
        }
        
        levelText.setText("LEVEL " + currentLevel);
        
        for (int i = 0; i < coins.length; i++) {
            if (collectedCoins[i]) {
                coins[i].setVisible(false);
            } else {
                coins[i].setVisible(true);
                coins[i].setCenterX(coinX[i] - cameraX);
                coins[i].setCenterY(coinY[i]);
            }
        }

        canvas.setTranslateX(-cameraX);
        scoreText.setText(String.valueOf(p.getScore()));
        for (int i = 0; i < hearts.length; i++) {
            if (i < p.getLives()) {
                hearts[i].setVisible(true);
            } else {
                hearts[i].setVisible(false);
            }
        }
    }
}
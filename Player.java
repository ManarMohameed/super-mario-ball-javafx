package Fx;

public class Player {
    private double x, y;
    private double speedX = 0;
    private double speedY = 0;
    private boolean onGround = false;

    private static final double GRAVITY = 0.5;
    private static final double JUMP_FORCE = -12;
    private static final double MOVE_SPEED = 5;
    private static final double GROUND_Y = 340;

    private int score = 0;
    private int lives = 3; 
    
    private boolean fallingInPit = false;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveRight() { speedX = MOVE_SPEED; }
    public void moveLeft() { speedX = -MOVE_SPEED; }
    public void stop() { speedX = 0; }

    public void jump() {
        if (onGround) {
            speedY = JUMP_FORCE;
            onGround = false;
        }
    }
    
    public void startFallingIntoPit() {
        fallingInPit = true;
        speedY = 8; 
    }

    public boolean isDead() {
     return lives <= 0;
     }
    
    
    public void update() {
        if (fallingInPit) {
            speedY += 0.7;
            y += speedY;
            if (y > 600) {
                lives = 0;
            }
            return;
        }
        speedY += GRAVITY;
        y += speedY;
        if (y >= GROUND_Y) {
            y = GROUND_Y;
            speedY = 0;
            onGround = true;
        }
        x += speedX;
    }   

    public void loseLife() {
        lives--;
        x -= 50; 
        if (x < 15) x = 15;
    }
    
    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addScore(int points) { this.score += points; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    
    public void reset() {
        this.x = 40;
        this.y = 200;
        this.lives = 3;
        this.score = 0;
        this.speedX = 0;
        this.speedY = 0;

        this.fallingInPit = false;
        this.onGround = false;
    }
}
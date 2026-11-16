package Fx;

import java.io.*;
import java.nio.file.*;

public class ScoreManager {
    private static final String FILE_NAME = "highscore.txt";

    public static void saveHighScore(int score) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println(score);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static int loadHighScore() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            return Integer.parseInt(content.trim());
        } catch (Exception e) { return 0; }
    }
}
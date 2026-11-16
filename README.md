# Super Mario Ball – Multi-Levels

A 2D platform-style game built with **Java** and **JavaFX**. The player controls a red ball, collects coins, avoids moving enemies, and progresses through two levels.

## Features

- Two playable levels with different environments.
- Player movement, jumping, and gravity.
- Coin collection and score tracking (100 points per coin).
- Moving enemies and collision detection.
- Three lives, with a game-over screen when lives run out.
- A pit obstacle in Level 2.
- Camera scrolling through the level.
- Start, retry, and level-progression screens.

## Controls

| Key | Action |
| --- | --- |
| Right Arrow | Move right |
| Left Arrow | Move left |
| Space or Up Arrow | Jump |

## Requirements

- JDK 21 (or a compatible JDK)
- JavaFX SDK (the project was developed with JavaFX)
- An IDE such as IntelliJ IDEA or Eclipse, or a terminal

> JavaFX is a separate SDK. Make sure its `lib` directory is available when compiling and running the project.

## Run from the command line (Windows)

Open a terminal in the project folder. Replace `PATH_TO_FX` with the path to your JavaFX SDK `lib` directory.

Compile:

```powershell
javac --module-path "PATH_TO_FX" --add-modules javafx.controls -d out src/Fx/*.java
```

Run:

```powershell
java --module-path "PATH_TO_FX" --add-modules javafx.controls -cp out Fx.Main
```

## Project structure

```text
super-mario-ball-javafx/
├── src/
│   └── Fx/
│       ├── Main.java
│       ├── GameController.java
│       ├── GameView.java
│       ├── Player.java
│       └── ScoreManager.java
├── assets/
│   └── gameplay-demo.mp4
├── .gitignore
└── README.md
```

## Demo

A gameplay recording is included in [`assets/gameplay-demo.mp4`](assets/gameplay-demo.mp4).

## Notes

- `ScoreManager.java` is included as a high-score file utility, but it is not currently connected to the main gameplay flow.
- This repository contains the game-related source files only. Two additional Java files in the supplied ZIP (`GameWindow.java` and `MovieTimeConverter.java`) were left out because they are separate exercises and are not used by the game entry point.

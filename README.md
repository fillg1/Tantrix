# HexaTiles

A Java implementation of a hexagonal tile-based puzzle game solver.

## Description

This project implements an automated solver that places tiles on a hexagonal grid to form valid configurations where adjacent tile edges match in color.

**Based on the board game Tantrix by Mike McManaway**

## Features

- Automatic puzzle solving for levels 3-30
- Visual Swing-based UI
- Support for all 30 hexagonal tiles
- Screenshot functionality

## Requirements

- Java 21 or higher
- Gradle 8.x (wrapper included)

## Build

```bash
./gradlew build
```

## Run

```bash
./gradlew run
```

Or run the main class directly:

```bash
java -jar build/libs/HexaTiles.jar
```

## Usage

1. Select the level (number of pieces to use, 3-30)
2. Click "start" to solve the puzzle
3. Click "screenshot" to save a screenshot of the current board

## Project Structure

```
src/main/java/illgner/ch/hexatiles/
├── Board.java       # Hexagonal grid management
├── Color.java       # Color enumeration
├── Coordinate.java  # Hexagonal coordinate system
├── Game.java        # Game logic and solver
├── GameDialog.java  # Swing UI
└── Piece.java       # Tile definitions and rendering
```

## References

- [Tantrix - Wikipedia](https://en.wikipedia.org/wiki/Tantrix)

## License

MIT License

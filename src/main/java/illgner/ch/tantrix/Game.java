package illgner.ch.tantrix;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Game {

    private static final Coordinate START = new Coordinate(5, 5);

    private final Board board = new Board();

    private Component parent = null;

    private Color color = null;

    public Game(Component parent) {
        this.parent = parent;
    }

    public boolean start(int n) {
        if (n <= 0 || n > Piece.Pieces.length) {
            System.err.println("Invalid level: " + n + ". Must be between 1 and " + Piece.Pieces.length);
            return false;
        }
        
        board.clear();
        Piece[] pieces = new Piece[n];
        Random rnd = new Random();

        System.arraycopy(Piece.Pieces, 0, pieces, 0, n);

        for (int i = 0; i < rnd.nextInt(1024); i++) {
            int i1 = rnd.nextInt(n);
            int i2 = rnd.nextInt(n);
            Piece t = pieces[i1];
            pieces[i1] = pieces[i2];
            pieces[i2] = t;
        }

        Piece[] unusedPieces = new Piece[n - 1];
        for (int i = 0; i < n - 1; i++) {
            Piece piece = pieces[i];
            piece.setOrientation(0);
            unusedPieces[i] = piece;
        }
        Piece firstPiece = pieces[n - 1];
        firstPiece.setOrientation(0);
        // Use the actual first piece's color (post-shuffle), not the static index
        this.color = firstPiece.getColor();
        System.out.println(n + " " + this.color);
        Coordinate firstCoordinate = START;
        board.setPiece(firstPiece, firstCoordinate);
        Coordinate firstNeighbour = null;
        for (int i = 0; i < 6; i++) {
            if (firstPiece.getSide(i) == color) {
                firstNeighbour = firstCoordinate.getNeighbour(i);
                break; // Found first matching neighbor, no need to continue
            }
        }
        
        if (firstNeighbour == null) {
            System.err.println("Warning: Could not find valid first neighbor for piece");
            return false;
        }
        
        refresh();
        return solve(firstCoordinate, firstNeighbour, unusedPieces);
    }

    public void draw(Graphics2D g2) {
        board.draw(g2);
    }

    public void refresh() {
        SwingUtilities.invokeLater(() -> parent.repaint());
    }

    private boolean solve(Coordinate prev, Coordinate next, Piece[] pieces) {
        if (next == null || pieces == null) {
            return false;
        }
        
        for (int p = 0; p < pieces.length; p++) {
            Piece piece = pieces[p];
            board.setPiece(piece, next);
            for (int i = 0; i < 6; i++) {
                piece.setOrientation(i);
                if (board.check(next)) {
                    // get next neighbour
                    Coordinate neighbour = null;
                    for (int j = 0; j < 6; j++) {
                        if (piece.getSide(j) == this.color) {
                            neighbour = next.getNeighbour(j);
                            if (neighbour != null && !neighbour.equals(prev)) {
                                break;
                            }
                            neighbour = null;
                        }
                    }
                    if (neighbour != null) {
                        int distance = (START.x - neighbour.x) * (START.x - neighbour.x) + (START.y - neighbour.y) * (START.y - neighbour.y);
                        int size = pieces.length;

                        if (distance < size * size) {
                            if (pieces.length == 1) {
                                if (board.getPiece(neighbour) != null && check())
                                    return true;
                            } else {
                                Piece[] unused = new Piece[pieces.length - 1];
                                System.arraycopy(pieces, 0, unused, 0, p);
                                System.arraycopy(pieces, p + 1, unused, p, pieces.length - p - 1);
                                if (board.getPiece(neighbour) == null && solve(next, neighbour, unused))
                                    return true;
                            }
                        } else {
                            board.setPiece(null, next);
                            return false;
                        }
                    }
                }
            }
        }
        refresh();
        board.setPiece(null, next);
        return false;
    }

    private boolean check() {
        return board.checkHoles();
    }
}

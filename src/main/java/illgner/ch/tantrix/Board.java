package illgner.ch.tantrix;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.HashSet;

public class Board {

    private final Piece[][] pieces = new Piece[Coordinate.SIZE][Coordinate.SIZE];

    public void setPiece(Piece piece, Coordinate coor) {
        if (coor == null) {
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        pieces[coor.x][coor.y] = piece;
    }

    public Piece getPiece(Coordinate coor) {
        if (coor == null) {
            return null;
        }
        return pieces[coor.x][coor.y];
    }

    void draw(Graphics2D g) {
        for (int i = 0; i < Coordinate.SIZE; i++)
            for (int j = 0; j < Coordinate.SIZE; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                if (pieces[i][j] != null)
                    try {
                        ((Piece) pieces[i][j].clone()).draw(g, coordinate);
                    } catch (CloneNotSupportedException e) {
                        // Log error and continue rendering without this piece
                        System.err.println("Warning: Could not clone piece at " + coordinate + ": " + e.getMessage());
                    }
                else
                    Piece.drawHexagon(g, coordinate);
            }
    }

    public Collection<Coordinate> getFreeCoordinates() {
        Collection<Coordinate> free = new HashSet<>();

        for (int x = 0; x < Coordinate.SIZE; x++)
            for (int y = 0; y < Coordinate.SIZE; y++) {
                if (pieces[x][y] != null) {
                    Coordinate xy = new Coordinate(x, y);
                    for (int o = 0; o < 6; o++) {
                        Coordinate neighbour = xy.getNeighbour(o);
                        if (neighbour != null && pieces[neighbour.x][neighbour.y] == null)
                            free.add(neighbour);
                    }
                }
            }

        return free;
    }

    public boolean check(Coordinate coordinate) {
        Piece piece = pieces[coordinate.x][coordinate.y];
        for (int o = 0; o < 6; o++) {
            Coordinate neighbour = coordinate.getNeighbour(o);
            if (neighbour != null) {
                Piece neighbourPiece = pieces[neighbour.x][neighbour.y];
                if (neighbourPiece != null) {
                    Color myColor = piece.getSide(o);
                    Color yourColor = neighbourPiece.getSide((o + 3) % 6);
                    if (myColor != yourColor)
                        return false;
                } else {
                    // check empty field
                    for (Color c : Color.values()) {
                        int num = 0;
                        for (int p = 0; p < 6; p++) {
                            Coordinate coord = neighbour.getNeighbour(p);
                            if (coord != null) {
                                Piece piece2 = pieces[coord.x][coord.y];
                                if (piece2 != null) {
                                    if (piece2.getSide((p + 3) % 6) == c) {
                                        num++;
                                        if (num > 2) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void clear() {
        for (int x = 0; x < Coordinate.SIZE; x++)
            for (int y = 0; y < Coordinate.SIZE; y++) {
                pieces[x][y] = null;
            }
    }

    public boolean checkHoles() {
        boolean[][] visited = new boolean[Coordinate.SIZE][Coordinate.SIZE];
        boolean[][] tovisitMarker = new boolean[Coordinate.SIZE][Coordinate.SIZE];
        // count empty fields
        int nrEmpty = 0;
        Coordinate empty = null;
        for (int x = 0; x < Coordinate.SIZE; x++)
            for (int y = 0; y < Coordinate.SIZE; y++) {
                visited[x][y] = false;
                tovisitMarker[x][y] = false;
                if (pieces[x][y] == null) {
                    nrEmpty++;
                    empty = new Coordinate(x, y);
                }
            }
        if (empty == null)
            return true;
        int size = Coordinate.SIZE;
        Coordinate[] tovisit = new Coordinate[size * size];
        int nrToVisit = 0;
        visited[empty.x][empty.y] = true;
        for (int i = 0; i < 6; i++) {
            Coordinate neighbour = empty.getNeighbour(i);
            if (neighbour != null && getPiece(neighbour) == null)
                tovisit[nrToVisit++] = neighbour;
        }
        while (nrToVisit > 0) {
            Coordinate actual = tovisit[--nrToVisit];
            for (int i = 0; i < 6; i++) {
                Coordinate neighbour = actual.getNeighbour(i);
                visited[actual.x][actual.y] = true;
                if (neighbour != null && getPiece(neighbour) == null && !visited[neighbour.x][neighbour.y] && !tovisitMarker[neighbour.x][neighbour.y]) {
                    tovisitMarker[neighbour.x][neighbour.y] = true;
                    tovisit[nrToVisit++] = neighbour;
                }
            }
        }
        int nrVisited = 0;
        for (int x = 0; x < Coordinate.SIZE; x++)
            for (int y = 0; y < Coordinate.SIZE; y++)
                if (visited[x][y])
                    nrVisited++;

        return nrVisited == nrEmpty;
    }

}

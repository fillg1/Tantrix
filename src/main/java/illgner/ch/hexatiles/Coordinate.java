package illgner.ch.hexatiles;

public class Coordinate {

    public final static int SIZE = 50;

    private final static int[] xDir = {-1, 0, 1, 1, 0, -1};
    private final static int[] yoDir = {1, 1, 1, 0, -1, 0};
    private final static int[] yeDir = {0, 1, 0, -1, -1, -1};
    private final static Coordinate[][][] neighbours = new Coordinate[SIZE][SIZE][6];

    static {
        for (int x = 0; x < SIZE; x++)
            for (int y = 0; y < SIZE; y++)
                for (int o = 0; o < 6; o++) {

                    int xn = x + xDir[o];
                    int yn = y + ((x % 2 == 0) ? yeDir[o] : yoDir[o]);

                    if (xn >= 0 && xn < SIZE && yn >= 0 && yn < SIZE)
                        neighbours[x][y][o] = new Coordinate(xn, yn);
                    else
                        neighbours[x][y][o] = null;
                }
    }

    int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            Coordinate rls = (Coordinate) o;
            return rls.x == x && rls.y == y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        // Provide a better distribution than bitwise OR
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Coordinate getNeighbour(int orientation) {
        return neighbours[x][y][orientation];
    }
}

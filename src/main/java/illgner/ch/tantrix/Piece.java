package illgner.ch.tantrix;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import static java.awt.Color.BLACK;
import static java.awt.Color.LIGHT_GRAY;

public class Piece implements Cloneable {

    private final static double Y = Math.sqrt(3.0) / 2.0;
    private final static double SIZE = 30.0;

    private final static double Xcoord[] = {-1.0, -0.5, 0.5, 1.0, 0.5, -0.5};
    private final static double Ycoord[] = {0.0, Y, Y, 0.0, -Y, -Y};
    private final static double Xmid[] = {-0.75, 0.0, 0.75, 0.75, 0.0, -0.75};
    private final static double Ymid[] = {Y / 2, Y, Y / 2, -Y / 2, -Y, -Y / 2};

    private Color color;

    private Color color1;
    private Color color2;
    private Color color3;
    private int start1;
    private int start2;
    private int start3;
    private int end1;
    private int end2;
    private int end3;

    private Color side[] = new Color[6];
    private int orientation = 0;

    private Piece(int s1, int e1, Color color1, int s2, int e2, Color color2, int s3, int e3, Color color3, Color color) {

        assert (0 <= s1);
        assert (s1 <= 5);
        assert (0 <= e1);
        assert (e1 <= 5);
        assert (0 <= s2);
        assert (s2 <= 5);
        assert (0 <= e2);
        assert (e2 <= 5);
        assert (0 <= s3);
        assert (s3 <= 5);
        assert (0 <= e3);
        assert (e3 <= 5);
        assert (s1 != s2);
        assert (s1 != s3);
        assert (s1 != e1);
        assert (s1 != e2);
        assert (s1 != e3);
        assert (s2 != s3);
        assert (s2 != e1);
        assert (s2 != e2);
        assert (s2 != e3);
        assert (s3 != e1);
        assert (s3 != e2);
        assert (s3 != e3);
        assert (e1 != e2);
        assert (e1 != e3);
        assert (e2 != e3);
        assert (color1 != color2);
        assert (color1 != color3);
        assert (color2 != color3);

        this.start1 = s1;
        this.start2 = s2;
        this.start3 = s3;
        this.end1 = e1;
        this.end2 = e2;
        this.end3 = e3;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color = color;

        side[start1] = color1;
        side[end1] = color1;
        side[start2] = color2;
        side[end2] = color2;
        side[start3] = color3;
        side[end3] = color3;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Piece p = (Piece) super.clone();
        for (int o = 0; o < 6; o++) {
            p.side[o] = side[o];
        }
        return p;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        assert (orientation >= 0);
        assert (orientation <= 5);
        this.orientation = orientation;
    }

    public Color getColor() {
        return color;
    }

    public static void drawHexagon(Graphics2D g, Coordinate c) {
        drawHexagon(g, c.x, c.y);
    }

    private static void drawHexagon(Graphics2D g, int x, int y) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1.0f));
        g.setColor(BLACK);

        double xOffset = 1.5 * x + 1.0;
        double yOffset = 2.0 * y * Y + (x % 2) * Y + 1.0;

        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, Xcoord.length);
        polygon.moveTo(Math.round(SIZE * (Xcoord[0] + xOffset)), Math.round(SIZE * (Ycoord[0] + yOffset)));

        for (int index = 1; index < Xcoord.length; index++) {
            polygon.lineTo(Math.round(SIZE * (Xcoord[index] + xOffset)), Math.round(SIZE * (Ycoord[index] + yOffset)));
        }

        polygon.closePath();
        g.draw(polygon);

        g.drawString(x + ":" + y, Math.round(SIZE * xOffset), Math.round(SIZE * yOffset));
    }

    public void draw(Graphics2D g, Coordinate c) {
        draw(g, c.x, c.y);
    }

    private void draw(Graphics2D g, int x, int y) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(LIGHT_GRAY);
        g.setStroke(new BasicStroke((float) (SIZE / 8.0), BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));

        double xOffset = 1.5 * x + 1.0;
        double yOffset = 2.0 * y * Y + (x % 2) * Y + 1.0;

        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, Xcoord.length);
        polygon.moveTo(Math.round(SIZE * (Xcoord[0] + xOffset)), Math.round(SIZE * (Ycoord[0] + yOffset)));

        for (int index = 1; index < Xcoord.length; index++) {
            polygon.lineTo(Math.round(SIZE * (Xcoord[index] + xOffset)), Math.round(SIZE * (Ycoord[index] + yOffset)));
        }

        polygon.closePath();
        g.draw(polygon);
        g.setPaint(BLACK);
        g.fill(polygon);

        g.setStroke(new BasicStroke((float) (SIZE / 4.0), BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));

        drawPath(g, xOffset, yOffset, start1, end1, color1);
        drawPath(g, xOffset, yOffset, start2, end2, color2);
        drawPath(g, xOffset, yOffset, start3, end3, color3);
    }

    private void drawPath(Graphics2D g, double xOffset, double yOffset, int start, int end, Color color) {

        assert (orientation >= 0);
        assert (orientation <= 5);

        start = (start + orientation) % 6;
        end = (end + orientation) % 6;

        if (start > end) {
            int t = end;
            end = start;
            start = t;
        }

        assert (start < end);
        assert (start >= 0);
        assert (start <= 5);
        assert (end >= 0);
        assert (end <= 5);
        g.setColor(color.getColor());

        switch (start) {
            case 0:
              switch (end) {
                case 1 -> draw01(g, xOffset, yOffset);
                case 2 -> draw02(g, xOffset, yOffset);
                case 3 -> draw03(g, xOffset, yOffset);
                case 4 -> draw04(g, xOffset, yOffset);
                case 5 -> draw05(g, xOffset, yOffset);
                default -> throw new RuntimeException("unknown start,end=" + start + "," + end);
              }
                break;
            case 1:
              switch (end) {
                case 2 -> draw12(g, xOffset, yOffset);
                case 3 -> draw13(g, xOffset, yOffset);
                case 4 -> draw14(g, xOffset, yOffset);
                case 5 -> draw15(g, xOffset, yOffset);
                default -> throw new RuntimeException("unknown start,end=" + start + "," + end);
              }
                break;
            case 2:
              switch (end) {
                case 3 -> draw23(g, xOffset, yOffset);
                case 4 -> draw24(g, xOffset, yOffset);
                case 5 -> draw25(g, xOffset, yOffset);
                default -> throw new RuntimeException("unknown start,end=" + start + "," + end);
              }
                break;
            case 3:
              switch (end) {
                case 4 -> draw34(g, xOffset, yOffset);
                case 5 -> draw35(g, xOffset, yOffset);
                default -> throw new RuntimeException("unknown start,end=" + start + "," + end);
              }
                break;
            case 4:
              if (end == 5) {
                draw45(g, xOffset, yOffset);
              } else {
                throw new RuntimeException("unknown start,end=" + start + "," + end);
              }
                break;
            default:
                throw new RuntimeException("unknown start=" + start);
        }
    }

    private void draw01(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (Xcoord[1] - 0.5 + xOffset), SIZE * (Ycoord[1] - 0.5 + yOffset), SIZE, SIZE, 0, 120, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw02(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (-1.5 + xOffset), SIZE * (2.0 * Y - 1.5 + yOffset), 3 * SIZE, 3 * SIZE, 60, 60, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw03(Graphics2D g, double xOffset, double yOffset) {
        Line2D line = new Line2D.Double(SIZE * (Xmid[0] + xOffset), SIZE * (Ymid[0] + yOffset), SIZE * (Xmid[3] + xOffset), SIZE * (Ymid[3] + yOffset));
        g.draw(line);
    }

    private void draw04(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (-3.0 + xOffset), SIZE * (-1.5 - Y + yOffset), 3 * SIZE, 3 * SIZE, 300, 60, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw12(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (Xcoord[2] - 0.5 + xOffset), SIZE * (Ycoord[2] - 0.5 + yOffset), SIZE, SIZE, 60, 120, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw13(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (xOffset), SIZE * (Y - 1.5 + yOffset), 3 * SIZE, 3 * SIZE, 120, 60, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw14(Graphics2D g, double xOffset, double yOffset) {
        Line2D line = new Line2D.Double(SIZE * (Xmid[1] + xOffset), SIZE * (Ymid[1] + yOffset), SIZE * (Xmid[4] + xOffset), SIZE * (Ymid[4] + yOffset));
        g.draw(line);
    }

    private void draw15(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (-3.0 + xOffset), SIZE * (-1.5 + Y + yOffset), 3 * SIZE, 3 * SIZE, 0, 60, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw23(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (Xcoord[3] - 0.5 + xOffset), SIZE * (Ycoord[3] - 0.5 + yOffset), SIZE, SIZE, 120, 120, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw24(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (xOffset), SIZE * (-Y - 1.5 + yOffset), 3 * SIZE, 3 * SIZE, 180, 60, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw25(Graphics2D g, double xOffset, double yOffset) {
        Line2D line = new Line2D.Double(SIZE * (Xmid[2] + xOffset), SIZE * (Ymid[2] + yOffset), SIZE * (Xmid[5] + xOffset), SIZE * (Ymid[5] + yOffset));
        g.draw(line);
    }

    private void draw34(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (Xcoord[4] - 0.5 + xOffset), SIZE * (Ycoord[4] - 0.5 + yOffset), SIZE, SIZE, 180, 120, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw35(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (-1.5 + xOffset), SIZE * (-1.5 - 2.0 * Y + yOffset), 3 * SIZE, 3 * SIZE, 240, 60, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw45(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (Xcoord[5] - 0.5 + xOffset), SIZE * (Ycoord[5] - 0.5 + yOffset), SIZE, SIZE, 240, 120, Arc2D.OPEN);
        g.draw(arc);
    }

    private void draw05(Graphics2D g, double xOffset, double yOffset) {
        Arc2D arc = new Arc2D.Double(SIZE * (Xcoord[0] - 0.5 + xOffset), SIZE * (Ycoord[0] - 0.5 + yOffset), SIZE, SIZE, 300, 120, Arc2D.OPEN);
        g.draw(arc);
    }

    public Color getSide(int side) {
        return this.side[(side - orientation + 6) % 6];
    }

    public final static Piece Pieces[] = {
            new Piece(0, 1, Color.Yellow, 2, 4, Color.Red, 3, 5, Color.Blue, Color.Yellow),    //  1
            new Piece(0, 1, Color.Red, 2, 5, Color.Blue, 3, 4, Color.Yellow, Color.Yellow),    //  2
            new Piece(0, 1, Color.Red, 2, 3, Color.Yellow, 4, 5, Color.Blue, Color.Yellow),    //  3
            new Piece(0, 4, Color.Red, 1, 3, Color.Yellow, 2, 5, Color.Blue, Color.Red),       //  4
            new Piece(0, 3, Color.Red, 1, 2, Color.Yellow, 4, 5, Color.Blue, Color.Red),       //  5
            new Piece(0, 4, Color.Blue, 1, 3, Color.Red, 2, 5, Color.Yellow, Color.Blue),      //  6
            new Piece(0, 4, Color.Yellow, 1, 2, Color.Blue, 3, 5, Color.Red, Color.Blue),      //  7
            new Piece(0, 1, Color.Blue, 2, 4, Color.Yellow, 3, 5, Color.Red, Color.Blue),      //  8
            new Piece(0, 3, Color.Red, 1, 5, Color.Blue, 2, 4, Color.Yellow, Color.Yellow),    //  9
            new Piece(0, 2, Color.Blue, 1, 3, Color.Red, 4, 5, Color.Yellow, Color.Red),       // 10
            new Piece(0, 2, Color.Blue, 1, 5, Color.Yellow, 3, 4, Color.Red, Color.Red),       // 11
            new Piece(0, 2, Color.Yellow, 1, 5, Color.Blue, 3, 4, Color.Red, Color.Yellow),    // 12
            new Piece(0, 1, Color.Blue, 2, 5, Color.Yellow, 3, 4, Color.Red, Color.Blue),      // 13
            new Piece(0, 5, Color.Red, 1, 2, Color.Blue, 3, 4, Color.Yellow, Color.Blue),      // 14
            new Piece(0, 1, Color.Green, 2, 5, Color.Red, 3, 4, Color.Yellow, Color.Red),      // 15
            new Piece(0, 1, Color.Green, 2, 5, Color.Yellow, 3, 4, Color.Red, Color.Red),      // 16
            new Piece(0, 2, Color.Red, 1, 3, Color.Green, 4, 5, Color.Yellow, Color.Yellow),   // 17
            new Piece(0, 2, Color.Red, 1, 5, Color.Green, 3, 4, Color.Yellow, Color.Red),      // 18
            new Piece(0, 2, Color.Green, 1, 5, Color.Yellow, 3, 4, Color.Red, Color.Red),      // 19
            new Piece(0, 2, Color.Green, 1, 3, Color.Yellow, 4, 5, Color.Red, Color.Yellow),   // 20
            new Piece(0, 1, Color.Green, 2, 3, Color.Yellow, 4, 5, Color.Red, Color.Yellow),   // 21
            new Piece(0, 1, Color.Yellow, 2, 5, Color.Green, 3, 4, Color.Red, Color.Yellow),   // 22
            new Piece(0, 1, Color.Yellow, 2, 3, Color.Green, 4, 5, Color.Red, Color.Yellow),   // 23
            new Piece(0, 1, Color.Green, 2, 5, Color.Blue, 3, 4, Color.Red, Color.Red),       // 24
            new Piece(0, 1, Color.Green, 2, 3, Color.Blue, 4, 5, Color.Red, Color.Red),        // 25
            new Piece(0, 3, Color.Green, 1, 2, Color.Blue, 4, 5, Color.Red, Color.Red),        // 26
            new Piece(0, 3, Color.Green, 1, 2, Color.Blue, 4, 5, Color.Red, Color.Red),        // 27
            new Piece(0, 1, Color.Green, 2, 3, Color.Red, 4, 5, Color.Blue, Color.Red),        // 28
            new Piece(0, 2, Color.Green, 1, 5, Color.Blue, 3, 4, Color.Red, Color.Red),       // 29,
            new Piece(0, 1, Color.Green, 2, 5, Color.Red, 3, 4, Color.Blue, Color.Red)         // 30
    };
}

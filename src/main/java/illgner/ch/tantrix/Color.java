package illgner.ch.tantrix;

public enum Color {

    Yellow(java.awt.Color.YELLOW), Red(java.awt.Color.RED), Blue(java.awt.Color.BLUE), Green(java.awt.Color.GREEN);

    private final java.awt.Color color;

    public java.awt.Color getColor() {
        return color;
    }

    Color(java.awt.Color color) {
        this.color = color;
    }
}

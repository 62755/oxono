package g62755.dev3.oxono.model;

public class Pawn {

    private Color color;
    private Symbol symbol;

    /**
     * Creates a pawn with the specified color and symbol.
     * @param color the color of the pawn
     * @param symbol the symbol of the pawn
     */
    public Pawn(Color color, Symbol symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    /**
     * Returns the color of the pawn.
     * @return the color of the pawn
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the symbol of the pawn.
     * @return the symbol of the pawn
     */
    public Symbol getSymbol() {
        return symbol;
    }
}

package g62755.dev3.oxono.model;

import java.util.Objects;

public class Totem extends Pawn {

    private Position position;

    /**
     * Creates a new totem with a specific color, symbol, and position.
     * @param color    the color of the totem
     * @param symbol   the symbol of the totem
     * @param position the initial position of the totem
     */
    public Totem(Color color, Symbol symbol, Position position) {
        super(color, symbol);
        this.position = position;
    }

    /**
     * Gets the current position of the totem on the board.

     * @return the position of the totem
     */
    public Position getPosition() {
        return position;
    }

    private void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Updates the totem's position to a new location on the board.
     * @param newPosition the new position of the totem
     */
    public void updateTotemPosition(Position newPosition) {
        setPosition(newPosition);
    }

    /**
     * Returns a string representation of the totem, including its symbol.
     * @return a string representation of the totem
     */
    @Override
    public String toString() {
        return "Totem " + getSymbol();
    }

    /**
     * Compares this totem to another object for equality.
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if both references point to the same object
        if (obj == null || getClass() != obj.getClass()) return false; // Check for null and class type
        Totem totem = (Totem) obj; // Cast to Totem
        return getColor() == totem.getColor() && // Compare color
                getSymbol() == totem.getSymbol() && // Compare symbol
                position.equals(totem.position); // Compare position
    }

    /**
     * Generates a hash code for the totem based on its attributes.
     * @return the hash code for this totem
     */
    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getSymbol(), position);
    }
}

package g62755.dev3.oxono.model;

import java.util.Objects;

public record Position(int x, int y) {

    /**
     * Returns a string representation of the position.
     * @return A string in the format "(x,y)".
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Checks if this position is equal to another object.
     * @param obj The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Returns the hash code of this position.
     * @return The hash code of this position.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

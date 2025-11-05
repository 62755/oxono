package g62755.dev3.oxono.model;

public class Player {

    private Color c;
    private int x = 8;
    private int o = 8;

    /**
     * Creates a player with a specified color and initializes the number of tokens based on the board size.
     * @param c    the color of the player
     * @param size the size of the board
     */
    public Player(Color c, int size) {
        this.c = c;
        this.x = (((size * size) - 4) / 4);
        this.o = (((size * size) - 4) / 4);
    }

    /**
     * Decreases the number of 'X' tokens for the player. Throws an exception if none are left.
     * @throws IllegalStateException if the player has no 'X' tokens left
     */
    public void decreaseX() {
        if (x > 0) {
            x--;
        } else {
            throw new IllegalStateException("The player has no X pawns left !");
        }
    }

    /**
     * Decreases the number of 'O' tokens for the player. Throws an exception if none are left.
     * @throws IllegalStateException if the player has no 'O' tokens left
     */
    public void decreaseO() {
        if (o > 0) {
            o--;
        } else {
            throw new IllegalStateException("The player has no O pawns left !");
        }
    }

    /**
     * Increases the number of 'X' tokens for the player.
     */
    public void increaseX() {
        x++;
    }

    /**
     * Increases the number of 'O' tokens for the player.
     */
    public void increaseO() {
        o++;
    }

    /**
     * Gets the color of the player.
     * @return the player's color
     */
    public Color getC() {
        return c;
    }

    /**
     * Gets the number of 'X' tokens remaining for the player.
     * @return the number of 'X' tokens
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the number of 'O' tokens remaining for the player.
     * @return the number of 'O' tokens
     */
    public int getO() {
        return o;
    }

    /**
     * Returns a string representation of the player's color.
     * @return the color of the player as a string
     */
    @Override
    public String toString() {
        return c.toString();
    }
}

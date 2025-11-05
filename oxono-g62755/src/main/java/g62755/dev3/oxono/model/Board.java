package g62755.dev3.oxono.model;

import g62755.dev3.util.CommandManager;

import java.util.Stack;

public class Board {

    private int size;
    private Pawn[][] grid;
    private CommandManager commandManager;

    /**
     * Initializes the game board with the specified size.
     * @param size the size of the board (number of rows and columns)
     */
    public Board(int size) {
        this.size = size;
        this.grid = new Pawn[size][size];
        this.commandManager = new CommandManager(new Stack<>(), new Stack<>());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.grid[i][j] = null;
            }
        }

        int mid = size / 2;
        this.grid[mid - 1][mid - 1] = new Totem(Color.BLUE, Symbol.X, new Position(mid-1, mid-1));
        this.grid[mid][mid] = new Totem(Color.BLUE, Symbol.O, new Position(mid, mid));
    }

    /**
     * Returns the size of the board.
     * @return the size of the board
     */
    public int getSize() {
        return size;
    }

    /**
     * Retrieves the pawn at a specific position.
     * @param row the row index of the position
     * @param col the column index of the position
     * @return the pawn at the given position or null if there is none
     * @throws IllegalArgumentException if the position is out of bounds
     */
    public Pawn getPawnAt(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Invalid position.");
        }
        return grid[row][col];
    }

    /**
     * Checks if the board is full.
     * @return true if the board is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (isEmpty(new Position(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a specific position is empty.
     * @param p the position to check
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty(Position p) {
        return this.grid[p.x()][p.y()] == null;
    }

    /**
     * Checks if a specific position is within the board's bounds.
     * @param p the position to check
     * @return true if the position is within bounds, false otherwise
     */
    public boolean isWithinBounds(Position p) {
        return p.x() >= 0 && p.x() < size && p.y() >= 0 && p.y() < size;
    }

    /**
     * Checks if a move from one position to another is valid.
     * @param from the starting position
     * @param to the target position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(Position from, Position to) {
        if (!isWithinBounds(to) || !isEmpty(to)) {
            return false;
        }

        if (isEnclaved(from)) {
            return isValidMoveWhileEnclaved(from, to);
        } else {
            return isValidMoveHorizontal(from, to) || isValidMoveVertical(from, to);
        }
    }

    private boolean isValidMoveHorizontal(Position from, Position to) {

        if (from.x() == to.x()) {
            int fromY = from.y();
            int toY = to.y();

            if (fromY < toY) {
                for (int y = fromY + 1; y < toY; y++) {
                    if (grid[from.x()][y] != null) {
                        return false;
                    }
                }
            } else {
                for (int y = fromY - 1; y > toY; y--) {
                    if (grid[from.x()][y] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidMoveVertical(Position from, Position to) {
        if (from.y() == to.y()) {
            int fromX = from.x();
            int toX = to.x();

            if (fromX < toX) {
                for (int x = fromX + 1; x < toX; x++) {
                    if (grid[x][from.y()] != null) {
                        return false;
                    }
                }
            } else {
                for (int x = fromX - 1; x > toX; x--) {
                    if (grid[x][from.y()] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if a totem is enclosed (surrounded on all sides by pawns).
     * @param p the position of the totem
     * @return true if the totem is enclosed, false otherwise
     */
    public boolean isEnclaved(Position p) {
        int x = p.x();
        int y = p.y();

        if (x > 0 && grid[x - 1][y] == null) {
            return false;
        }
        if (x < size - 1 && grid[x + 1][y] == null) {
            return false;
        }
        if (y > 0 && grid[x][y - 1] == null) {
            return false;
        }
        if (y < size - 1 && grid[x][y + 1] == null) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a move is valid while the totem is enclosed.
     * @param from the starting position
     * @param to the target position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMoveWhileEnclaved(Position from, Position to) {
        if (!isWithinBounds(to) || !isEmpty(to)) {
            return false;
        }

        int x = from.x();
        int y = from.y();

        if (areLineAndColumnFull(from)) {
            return true;
        }

        if (from.x() == to.x()) { // Déplacement horizontal
            int step = (y < to.y()) ? 1 : -1;
            boolean foundBarrier = false;
            for (int j = y + step; j != to.y() + step; j += step) {
                if (grid[x][j] != null) {
                    foundBarrier = true;
                } else if (foundBarrier) {
                    return j == to.y();
                }
            }
        } else if (from.y() == to.y()) { // Déplacement vertical
            int step = (x < to.x()) ? 1 : -1;
            boolean foundBarrier = false;
            for (int i = x + step; i != to.x() + step; i += step) {
                if (grid[i][y] != null) {
                    foundBarrier = true;
                } else if (foundBarrier) {
                    return i == to.x();
                }
            }
        }

        return false;
    }

    /**
     * Checks if both the row and column are full at a specific position.
     * @param p the position to check
     * @return true if both the row and column are full, false otherwise
     */
    public boolean areLineAndColumnFull(Position p) {
        int x = p.x();
        int y = p.y();

        for (int i = 0; i < size; i++) {
            if (grid[x][i] == null) {
                return false;
            }
        }
        for (int j = 0; j < size; j++) {
            if (grid[j][y] == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if inserting a token is valid.
     * @param posTotem the position of the totem
     * @param posToken the position where the token is to be inserted
     * @return true if the insertion is valid, false otherwise
     */
    public boolean isValidInsert(Position posTotem, Position posToken) {
        if (isEnclaved(posTotem) && isEmpty(posToken)) {
            return true;
        } else {
            return isWithinBounds(posToken) && isAdjacent(posTotem, posToken) && isEmpty(posToken);
        }
    }

    /**
     * Checks if two positions are adjacent.
     * @param p1 the first position
     * @param p2 the second position
     * @return true if the positions are adjacent, false otherwise
     */
    public boolean isAdjacent(Position p1, Position p2) {
        return (p1.x() == p2.x() && (p1.y() == p2.y() - 1 || p1.y() == p2.y() + 1)) ||
                (p1.y() == p2.y() && (p1.x() == p2.x() - 1 || p1.x() == p2.x() + 1));
    }

    /**
     * Moves a totem from one position to another.
     * @param t the totem to move
     * @param p the target position
     */
    public void moveTotem(Totem t, Position p) {
        Position from = t.getPosition();
        grid[from.x()][from.y()] = null;
        t.updateTotemPosition(p);
        grid[p.x()][p.y()] = t;
    }

    /**
     * Inserts a token at a specific position.
     * @param t the token to insert
     * @param posToken the position where the token is to be inserted
     */
    public void insertToken(Token t, Position posToken) {
        grid[posToken.x()][posToken.y()] = t;
    }

    /**
     * Removes a token from a specific position.
     * @param posToken the position of the token to be removed
     */
    public void removeToken(Position posToken) {
        grid[posToken.x()][posToken.y()] = null;
    }
}

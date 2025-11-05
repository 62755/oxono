package g62755.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(6);
    }

    @Test
    void getPawnAt() {
        Token pawn = new Token(Color.PINK, Symbol.X);
        board.insertToken(pawn, new Position(3,3));
        assertEquals(pawn, board.getPawnAt(3, 3));

        // Test invalid positions
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPawnAt(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPawnAt(0, 6);
        });
    }

    @Test
    void isEmpty() {
        Position position = new Position(0, 0);
        assertTrue(board.isEmpty(position));

        board.insertToken(new Token(Color.PINK, Symbol.X), position);
        assertFalse(board.isEmpty(position));

        board.removeToken(position);
        assertTrue(board.isEmpty(position));
    }


    @Test
    void isFull() {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                board.insertToken(new Token(Color.PINK, Symbol.X), new Position(i, j));
            }
        }
        assertTrue(board.isFull());

        // Remove a pawn
        board.removeToken(new Position(5, 5));
        assertFalse(board.isFull());
    }


    @Test
    void isWithinBounds() {
        assertTrue(board.isWithinBounds(new Position(3, 2)));
        assertFalse(board.isWithinBounds(new Position(6, 0)));
        assertFalse(board.isWithinBounds(new Position(-1, 10)));
    }

    @Test
    void isValidMove() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 1);

        // Free to position
        assertTrue(board.isValidMove(from, to));

        // Place a token at position (0,1) to make it occupied
        Token token= new Token(Color.PINK, Symbol.O);
        board.insertToken(token, to);

        // Occupied to position
        assertFalse(board.isValidMove(from, to));

        // Out of bounds position
        assertFalse(board.isValidMove(new Position(2, 2), new Position(10, 10)));
    }

    @Test
    void isValidMoveWhileEnclaved() {
        // Enclave totem
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0,1));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(1,0));
        Position from = new Position(0, 0);
        Totem totem = new Totem(Color.BLUE, Symbol.O, from);

        // Check Valid moves
        assertTrue(board.isValidMove(from, new Position(0, 2)));
        assertTrue(board.isValidMove(from, new Position(2, 0)));
        assertFalse(board.isValidMove(from, new Position(0, 3)));
        assertFalse(board.isValidMove(from, new Position(3, 0)));
    }

    @Test
    void isEnclaved() {
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(1, 2));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(2, 1));
        board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(2, 3));
        board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(3, 2));

        assertTrue(board.isEnclaved(new Position(2, 2)));

        board.removeToken(new Position(1, 2));
        assertFalse(board.isEnclaved(new Position(2, 2)));
    }

    @Test
    void areLineAndColumnFull() {
        for (int i = 0; i < 6; i++) {
            board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, i));
            board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(i, 0));
        }
        assertTrue(board.areLineAndColumnFull(new Position(0, 0)));

        board.removeToken(new Position(0, 3));
        assertFalse(board.areLineAndColumnFull(new Position(0, 0)));
    }

    @Test
    void isValidInsert() {
        Position posTotem = new Position(2, 2);
        Position posToken = new Position(2, 3);

        // Adjacent insert
        board.insertToken(new Token(Color.PINK, Symbol.X), posTotem);
        assertTrue(board.isValidInsert(posTotem, posToken));

        // Insert at an occupied position
        board.insertToken(new Token(Color.PINK, Symbol.X), posToken);
        assertFalse(board.isValidInsert(posTotem, posToken));

        // Insert at a non-adjacent position
        Position invalidPosition = new Position(0, 5);
        assertFalse(board.isValidInsert(posTotem, invalidPosition));

        // Insert at an out-of-bounds position
        Position outOfBoundsPosition = new Position(6, 6);
        assertFalse(board.isValidInsert(posTotem, outOfBoundsPosition));

        // Check inserts while enclaved
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(1, 2));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(2, 1));
        board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(2, 3));
        board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(3, 2));
        board.removeToken(new Position(0, 0));
        assertTrue(board.isValidInsert(posTotem, new Position(0, 0)));
        assertFalse(board.isValidInsert(posTotem, new Position(1, 2)));
    }


    @Test
    void isAdjacent() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 1);
        Position pos3 = new Position(1, 0);
        Position pos4 = new Position(1, 1);

        assertTrue(board.isAdjacent(pos1, pos2));
        assertFalse(board.isAdjacent(pos1, pos4));
        assertFalse(board.isAdjacent(pos2, pos3));
        assertTrue(board.isAdjacent(pos2, pos4));
    }

    @Test
    void moveTotem() {
        Totem totem = new Totem(Color.BLUE, Symbol.X, new Position(2, 2));

        // Move to a new position
        Position newPosition = new Position(2, 4);
        board.moveTotem(totem, newPosition);

        // Verify the move
        assertEquals(totem, board.getPawnAt(2, 4));
        assertNull(board.getPawnAt(2, 2));
    }

    @Test
    void insertToken() {
        Token token = new Token(Color.PINK, Symbol.X);
        Position position = new Position(2, 3);

        // Insert token
        board.insertToken(token, position);

        // Verify insertion
        assertEquals(token, board.getPawnAt(2, 3));
    }

    @Test
    void removeToken() {
        Token token = new Token(Color.PINK, Symbol.X);
        Position position = new Position(2, 3);

        // Insert and remove the token
        board.insertToken(token, position);
        board.removeToken(position);

        // Verify the removal
        assertNull(board.getPawnAt(2, 3));
    }

}
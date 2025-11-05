package g62755.dev3.oxono.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player pinkPlayer;
    private Player blackPlayer;
    private Board board;

    @BeforeEach
    void setUp() {
        pinkPlayer = new Player(Color.PINK, 6);
        blackPlayer = new Player(Color.BLACK, 6);
        board = new Board(6);
        game = new Game(blackPlayer, pinkPlayer, board);
    }

    @Test
    void testCheckWin() {
        // Same color and symbol (row)
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 1));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 2));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 3));
        assertTrue(game.checkWin(new Position(0, 3)));

        // Less than 4 pawns (row)
        board.removeToken(new Position(0, 3));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 4));
        assertFalse(game.checkWin(new Position(0, 2)));

        // Same color and symbol (column)
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(1, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(2, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(3, 0));
        assertTrue(game.checkWin(new Position(3, 0)));

        // Less than 4 pawns (column)
        board.removeToken(new Position(3, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(4, 0));
        assertFalse(game.checkWin(new Position(2, 0)));

        // Different colors, different symbols (row)
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.O), new Position(0, 1));
        board.insertToken(new Token(Color.BLACK, Symbol.X), new Position(0, 2));
        board.insertToken(new Token(Color.PINK, Symbol.O), new Position(0, 3));
        assertFalse(game.checkWin(new Position(0, 0)));

        // Same color, different symbols (row)
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.O), new Position(0, 1));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 2));
        board.insertToken(new Token(Color.PINK, Symbol.O), new Position(0, 3));
        assertTrue(game.checkWin(new Position(0, 1)));

        // Same color, different symbols (column)
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.O), new Position(1, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(2, 0));
        board.insertToken(new Token(Color.PINK, Symbol.O), new Position(3, 0));
        assertTrue(game.checkWin(new Position(1, 0)));

        // Same symbols but one of them is a totem
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(1, 0));
        Totem totem = new Totem(Color.BLUE, Symbol.X, new Position(2, 0));
        board.insertToken(new Token(Color.BLUE, Symbol.X), new Position(3, 0));
        assertTrue(game.checkWin(new Position(0, 0)));
    }

    @Test
    void drew() {
        // All pawns of same color and symbol
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                board.insertToken(new Token(Color.PINK, Symbol.X), new Position(i, j));
            }
        }
        assertTrue(game.drew());

        // No 4 pawns of the same color or symbol in a row
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i + j) % 2 == 0) {
                    board.insertToken(new Token(Color.PINK, Symbol.X), new Position(i, j));
                } else {
                    board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(i, j));
                }
            }
        }
        assertTrue(game.drew());

        // 4 pawns of the same color and symbol (board not full)
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 0));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 1));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 2));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(0, 3));
        for (int i = 0; i < 5; i++) {
            for (int j = 4; j < 6; j++) {
                board.removeToken(new Position(i, j));
            }
        }
        assertFalse(game.drew());

        // No 4 pawns of the same color or symbol in a row (board not full)
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if ((i + j) % 2 == 0) {
                    board.insertToken(new Token(Color.PINK, Symbol.X), new Position(i, j));
                } else {
                    board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(i, j));
                }
            }
        }
        board.removeToken(new Position(5, 0));
        assertFalse(game.drew());
    }

    @Test
    void surrender() {
        game.surrender();
        assertEquals(game.getGameState(), GameState.ENDED);
    }

    @Test
    void moveValid() {
        Totem totem = (Totem) game.getPawnAt(2,2);

        // Move to a valid position
        Position newPosition = new Position(0, 2);
        game.move(totem, newPosition);
        assertEquals(totem, game.getPawnAt(0, 2));
        assertNull(game.getPawnAt(0, 0));
    }

    @Test
    void moveOutOfBounds() {
        Totem totem = (Totem) game.getPawnAt(2, 2);

        Position invalidPosition = new Position(0, 6);

        assertThrows(IllegalArgumentException.class, () -> {
            game.move(totem, invalidPosition);
        });
    }

    @Test
    void moveWhileEnclaved() {
        Totem totem = (Totem) game.getPawnAt(2,2);

        // Enclave the totem and try valid move while enclaved
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(1, 2));
        board.insertToken(new Token(Color.PINK, Symbol.X), new Position(2, 1));
        board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(2, 3));
        board.insertToken(new Token(Color.BLACK, Symbol.O), new Position(3, 2));
        game.move(totem, new Position(2, 0));
        assertEquals(totem, game.getPawnAt(2, 0));
    }

    @Test
    void testInsert() {
        Token token = new Token(Color.PINK, Symbol.X);

        Totem totem = (Totem) game.getPawnAt(2,2);
        game.move(totem, new Position(2,3));

        // Valid adjacent insertion
        game.insert(token, totem.getPosition(), new Position(2,4));
        assertEquals(token, game.getPawnAt(2, 4));

        // Invalid non-adjacent insertion
        game.insert(token, totem.getPosition(), new Position(5,5));
        assertNotEquals(token, game.getPawnAt(5, 5));

        // Insertion at an occupied position
        Position posOccupied = new Position(3, 3);
        game.insert(token, totem.getPosition(), posOccupied);
        assertNotEquals(token, game.getPawnAt(3, 3));
    }

    @Test
    void testUndo() {
        // Setup initial move
        Totem totem = (Totem) game.getPawnAt(2,2);
        game.move(totem, new Position(2, 5));
        assertNull(game.getPawnAt(2, 2));
        assertEquals(totem, game.getPawnAt(2, 5));

        // Undo move
        game.undo();
        assertEquals(totem, game.getPawnAt(2, 2));
        assertNull(game.getPawnAt(2, 5));
        assertEquals(GameState.MOVE, game.getGameState());
    }

    @Test
    void testRedo() {
        // Setup initial move
        Totem totem = (Totem) game.getPawnAt(2,2);
        game.move(totem, new Position(2, 5));
        assertNull(game.getPawnAt(2, 2));
        assertEquals(totem, game.getPawnAt(2, 5));

        // Undo move
        game.undo();
        assertEquals(totem, game.getPawnAt(2, 2));
        assertNull(game.getPawnAt(2, 5));
        assertEquals(GameState.MOVE, game.getGameState());

        // Redo move
        game.redo();
        assertNull(game.getPawnAt(2, 2));
        assertEquals(totem, game.getPawnAt(2, 5));
    }
}


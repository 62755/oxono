package g62755.dev3.oxono.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI {

    private Game game;
    private Random random;

    /**
     * Initializes an AI instance with a reference to the game and a random generator.
     * @param game the game instance
     * @param random a random number generator
     */
    public AI(Game game, Random random) {
        this.game = game;
        this.random = random;
    }

    /**
     * Performs an action for the AI based on the current state of the game.
     */
    public void play() {
        if (game.getGameState() == GameState.MOVE) {
            playRandomMove();
        } else if (game.getGameState() == GameState.INSERT) {
            playRandomInsert();
        }
    }

    private void playRandomMove() {
        List<Position> allPositions = new ArrayList<>();
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                allPositions.add(new Position(i, j));
            }
        }

        boolean moveMade = false;
        Symbol symbolToMove = (random.nextInt(2) == 0) ? Symbol.X : Symbol.O;

        if (symbolToMove == Symbol.X && game.getToPlay().getX() <= 0) {
            symbolToMove = Symbol.O;
        } else if (symbolToMove == Symbol.O && game.getToPlay().getO() <= 0) {
            symbolToMove = Symbol.X;
        }

        while (!moveMade && !allPositions.isEmpty()) {
            int index = random.nextInt(allPositions.size());
            Position newPosition = allPositions.remove(index);

            Totem totemToMove = null;
            Position currentPosition = null;

            for (int i = 0; i < game.getBoardSize(); i++) {
                for (int j = 0; j < game.getBoardSize(); j++) {
                    Pawn pawn = game.getPawnAt(i, j);
                    if (pawn instanceof Totem && pawn.getSymbol() == symbolToMove) {
                        totemToMove = (Totem) pawn;
                        currentPosition = new Position(i, j);
                        break;
                    }
                }
                if (totemToMove != null) {
                    break;
                }
            }
            if (totemToMove != null && game.isValidMove(currentPosition, newPosition)) {
                game.move(totemToMove, newPosition);
                moveMade = true;
            }
        }

        if (!moveMade) {
            throw new IllegalStateException("No valid move found for AI.");
        }
    }

    private void playRandomInsert() {
        Position totemPosition = game.getLastTotemPosition();
        List<Position> allPositions = new ArrayList<>();
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.getPawnAt(i, j) == null) {
                    allPositions.add(new Position(i, j));
                }
            }
        }

        boolean insertMade = false;

        while (!insertMade && !allPositions.isEmpty()) {
            int index = random.nextInt(allPositions.size());
            Position insertPosition = allPositions.get(index);

            if (game.isValidInsert(totemPosition, insertPosition)) {
                Token token = new Token(game.getToPlay().getC(), game.getToInsert());
                game.insert(token, totemPosition, insertPosition);
                insertMade = true;

                game.checkWin(insertPosition);
            } else {
                allPositions.remove(index);
            }
        }

        if (!insertMade) {
            throw new IllegalStateException("No valid insertion found for AI.");
        }
    }
}

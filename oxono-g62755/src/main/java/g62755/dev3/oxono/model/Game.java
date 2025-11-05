package g62755.dev3.oxono.model;

import g62755.dev3.util.Command;
import g62755.dev3.util.CommandManager;
import g62755.dev3.util.Observable;
import g62755.dev3.util.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game implements Observable {

    private Player black;
    private Player pink;
    private Player toPlay;
    private Symbol toInsert;
    private Symbol lastSymbolMovedByPink;
    private Board board;
    private GameState gameState;
    private Position lastTotemPosition;
    private Position lastTotemPositionPink;
    private Position lastInsertedPosition;
    private CommandManager commandManager;
    private Stack<Token> undoInsertTokens = new Stack<>();
    private Stack<Token> redoInsertTokens = new Stack<>();
    private List<Observer> observers = new ArrayList<>();

    /**
     * Initializes a new game with two players and a board.
     * @param black the black player
     * @param pink the pink player
     * @param board the game board
     */
    public Game(Player black, Player pink, Board board) {
        this.black = black;
        this.pink = pink;
        this.toPlay = pink;
        this.board = board;
        this.gameState = GameState.MOVE;
        this.commandManager = new CommandManager(new Stack<>(), new Stack<>());
    }

    /**
     * Returns the size of the board.
     * @return the size of the board
     */
    public int getBoardSize() {
        return board.getSize();
    }

    /**
     * Returns the player whose turn it is to play.
     * @return the current player
     */
    public Player getToPlay() {
        return toPlay;
    }

    /**
     * Returns the symbol to be inserted after a move.
     * @return the symbol to insert
     */
    public Symbol getToInsert() {
        return toInsert;
    }

    /**
     * Returns the current state of the game.
     * @return the game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Retrieves the pawn at a specific position on the board.
     * @param x the row index of the position
     * @param y the column index of the position
     * @return the pawn at the specified position, or null if none exists
     */
    public Pawn getPawnAt(int x, int y) {
        return board.getPawnAt(x, y);
    }

    /**
     * Returns the last position of a moved totem.
     * @return the last position of the totem
     */
    public Position getLastTotemPosition() {
        return lastTotemPosition;
    }

    /**
     * Returns the last symbol moved by the pink player.
     * @return the last symbol moved by the pink player
     */
    public Symbol getLastSymbolMovedByPink() {
        return lastSymbolMovedByPink;
    }

    /**
     * Returns the last position of a totem moved by the pink player.
     * @return the last position of the pink player's totem
     */
    public Position getLastTotemPositionPink() {
        return lastTotemPositionPink;
    }

    private void switchPlayer() {
        if (gameState == GameState.ENDED) {
            return;
        }
        if (toPlay == pink) {
            toPlay = black;
        } else {
            toPlay = pink;
        }
    }

    private void mustMove(boolean move) {
        if (move) {
            gameState = GameState.MOVE;
        } else {
            gameState = GameState.INSERT;
        }
    }

    /**
     * Checks if a specific position is within the board's bounds.
     * @param p the position to check
     * @return true if the position is within bounds, false otherwise
     */
    public boolean isWithinBounds(Position p) {
        return board.isWithinBounds(p);
    }

    /**
     * Checks if a totem move is valid.
     * @param from the current position of the totem
     * @param to the target position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(Position from, Position to) {
        return board.isValidMove(from, to);
    }

    /**
     * Checks if inserting a token is valid.
     * @param posTotem the position of the totem
     * @param posToken the position where the token is to be inserted
     * @return true if the insertion is valid, false otherwise
     */
    public boolean isValidInsert(Position posTotem, Position posToken) {
        return board.isValidInsert(posTotem, posToken);
    }

    /**
     * Checks if the game is won.
     * @return true if the game is won, false otherwise
     */
    public boolean won() {
        return lastInsertedPosition != null && checkWin(lastInsertedPosition);
    }

    /**
     * Checks if the game is a draw (no winner).
     * @return true if the game is a draw, false otherwise
     */
    public boolean drew() {
        return !won() && board.isFull();
    }

    /**
     * Checks if the game has ended.
     * @return true if the game has ended, false otherwise
     */
    public boolean isEnded() {
        return gameState == GameState.ENDED;
    }

    /**
     * Ends the game, marking the current state as finished.
     */
    public void surrender() {
        gameState = GameState.ENDED;
    }

    /**
     * Checks if the game has been won by evaluating the last inserted position.
     * If a win condition is met, the game state is updated to "ENDED".
     * @param lastInsertedPosition the position of the last inserted token
     * @return true if the game has been won, false otherwise
     */
    public boolean checkWin(Position lastInsertedPosition) {
        boolean won = checkWinDirection(lastInsertedPosition);

        if (won) {
            endGame();
        }
        return won;
    }

    private boolean checkWinDirection(Position pos) {
        Pawn currentPawn = board.getPawnAt(pos.x(), pos.y());
        if (currentPawn == null || currentPawn instanceof Totem) {
            return false;
        }

        Symbol symbol = currentPawn.getSymbol();
        Color color = currentPawn.getColor();

        // Vérification horizontale
        int horizontalSymbolCount = 1;
        int horizontalColorCount = 1;

        horizontalSymbolCount += countInDirection(pos, 0, -1, symbol, null); // Vers la gauche
        horizontalSymbolCount += countInDirection(pos, 0, 1, symbol, null);  // Vers la droite

        horizontalColorCount += countInDirection(pos, 0, -1, null, color); // Vers la gauche
        horizontalColorCount += countInDirection(pos, 0, 1, null, color);  // Vers la droite

        if (horizontalSymbolCount >= 4 || horizontalColorCount >= 4) {
            return true;
        }

        // Vérification verticale
        int verticalSymbolCount = 1;
        int verticalColorCount = 1;

        verticalSymbolCount += countInDirection(pos, -1, 0, symbol, null); // Vers le haut
        verticalSymbolCount += countInDirection(pos, 1, 0, symbol, null);  // Vers le bas

        verticalColorCount += countInDirection(pos, -1, 0, null, color); // Vers le haut
        verticalColorCount += countInDirection(pos, 1, 0, null, color);  // Vers le bas

        return verticalSymbolCount >= 4 || verticalColorCount >= 4;
    }

    private int countInDirection(Position start, int dx, int dy, Symbol symbol, Color color) {
        int count = 0;
        int x = start.x() + dx;
        int y = start.y() + dy;

        while (board.isWithinBounds(new Position(x, y))) {
            Pawn nextPawn = board.getPawnAt(x, y);
            if (nextPawn != null && !(nextPawn instanceof Totem)) {
                if (symbol != null && nextPawn.getSymbol() == symbol) {
                    count++;
                } else if (color != null && nextPawn.getColor() == color) {
                    count++;
                } else {
                    break;
                }
                x += dx;
                y += dy;
            } else {
                break;
            }
        }

        return count;
    }

    private void endGame() {
        gameState = GameState.ENDED;
    }

    private Totem findTotemBySymbol(Symbol symbol) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Pawn pawn = getPawnAt(row, col);
                if (pawn instanceof Totem && pawn.getSymbol() == symbol) {
                    return (Totem) pawn;
                }
            }
        }
        return null;
    }

    /**
     * Moves a totem to a new position if the move is valid.
     * @param totem the totem to be moved
     * @param newPos the new position for the totem
     * @return true if the move was successful, false otherwise
     */
    public boolean move(Totem totem, Position newPos) {
        if (gameState != GameState.MOVE) {
            return false;
        }

        if ((totem.getSymbol() == Symbol.X && toPlay.getX() == 0) ||
                (totem.getSymbol() == Symbol.O && toPlay.getO() == 0)) {
            throw new IllegalStateException("You do not have enough tokens of this type to make this move.");
        }

        Totem totemSymbol = findTotemBySymbol(totem.getSymbol());
        if (totemSymbol == null) {
            return false;
        }

        Position currentPos = totemSymbol.getPosition();
        if (!board.isValidMove(currentPos, newPos)) {
            throw new IllegalArgumentException("Invalid Move.");
        }

        if (board.isWithinBounds(newPos) && board.isEmpty(newPos)) {
            Command moveCommand = new MoveCommand(board, totemSymbol, currentPos, newPos);
            commandManager.doIt(moveCommand);
            board.moveTotem(totem, newPos);
            lastTotemPosition = newPos;
            toInsert = totemSymbol.getSymbol();
            gameState = GameState.INSERT;

            if (toPlay == pink) {
                lastSymbolMovedByPink = totemSymbol.getSymbol();
                lastTotemPositionPink = newPos;
            }
            notifyObservers();
            System.out.println("Observer notified : Moving totem " + totemSymbol + " from " + currentPos + " to " + newPos + ".");
            return true;
        } else {
            throw new IllegalArgumentException("Invalid Move.");
        }
    }

    /**
     * Inserts a token at a specified position if the insertion is valid.
     * @param token the token to insert
     * @param posTotem the position of the totem
     * @param posToken the position where the token will be inserted
     * @return true if the insertion was successful, false otherwise
     */
    public boolean insert(Token token, Position posTotem, Position posToken) {
        if (gameState != GameState.INSERT) {
            return false;
        }

        if (board.isWithinBounds(posToken) && board.isEmpty(posToken)) {
            boolean enclaved = board.isEnclaved(posTotem);

            if (enclaved || board.isValidInsert(posTotem, posToken)) {
                Command insertCommand = new InsertCommand(board, token, posToken);
                commandManager.doIt(insertCommand);
                board.insertToken(token, posToken);
                lastInsertedPosition = posToken;
                undoInsertTokens.push(token);

                if (token.getSymbol() == Symbol.X) {
                    getToPlay().decreaseX();
                } else if (token.getSymbol() == Symbol.O) {
                    getToPlay().decreaseO();
                }

                switchPlayer();
                gameState = GameState.MOVE;
                notifyObservers();
                System.out.println("Observer notified : Inserting token " + token.getSymbol() + " at " + lastInsertedPosition + ".");
                return true;
            } else {
                throw new IllegalArgumentException("Invalid insert.");
            }
        } else {
            throw new IllegalArgumentException("Invalid insert.");
        }
    }

    /**
     * Undoes the last command, restoring the previous state of the game.
     */
    public void undo() {
        if (commandManager.getUndoStack().isEmpty()) {
            throw new IllegalStateException("No command to undo.");
        }
        if (gameState == GameState.ENDED) {
            throw new IllegalStateException("Game ended.");
        }

        if (gameState == GameState.INSERT) {
            commandManager.undo();
            mustMove(true);
        } else if (gameState == GameState.MOVE) {
            if (!undoInsertTokens.isEmpty()) {
                Token aiToken = undoInsertTokens.pop();
                if (aiToken.getSymbol() == Symbol.X) {
                    black.increaseX();
                } else if (aiToken.getSymbol() == Symbol.O) {
                    black.increaseO();
                }
                redoInsertTokens.push(aiToken);
            }
            commandManager.undo();
            commandManager.undo();
            commandManager.undo();
            if (!undoInsertTokens.isEmpty()) {
                Token playerToken = undoInsertTokens.pop();
                if (playerToken.getSymbol() == Symbol.X) {
                    pink.increaseX();
                } else if (playerToken.getSymbol() == Symbol.O) {
                    pink.increaseO();
                }
                redoInsertTokens.push(playerToken);
            }

            mustMove(false);
        }
        notifyObservers();
        System.out.println("Observer notified : Undoing last command.");
    }

    /**
     * Redoes the last undone command, restoring the subsequent state of the game.
     */
    public void redo() {
        if (commandManager.getRedoStack().isEmpty()) {
            throw new IllegalStateException("No command to redo.");
        }
        if (gameState == GameState.ENDED) {
            throw new IllegalStateException("Game ended.");
        }

        if (gameState == GameState.INSERT) {
            commandManager.redo();
            if (!redoInsertTokens.isEmpty()) {
                Token playerToken = redoInsertTokens.pop();
                if (playerToken.getSymbol() == Symbol.X) {
                    pink.decreaseX();
                } else if (playerToken.getSymbol() == Symbol.O) {
                    pink.decreaseO();
                }
            }
            commandManager.redo();
            commandManager.redo();
            if (!redoInsertTokens.isEmpty()) {
                Token aiToken = redoInsertTokens.pop();
                if (aiToken.getSymbol() == Symbol.X) {
                    black.decreaseX();
                } else if (aiToken.getSymbol() == Symbol.O) {
                    black.decreaseO();
                }
            }

            mustMove(true);
        } else if (gameState == GameState.MOVE) {
            commandManager.redo();
            mustMove(false);
        }
        notifyObservers();
        System.out.println("Observer notified : Redoing last command.");
    }

    /**
     * Counts the number of empty tiles on the board.
     * @return the number of empty tiles
     */
    public int countEmptyTiles() {
        int emptyTiles = 0;
        for (int i =0; i < getBoardSize(); i++) {
            for (int j =0; j < getBoardSize(); j++) {
                if (getPawnAt(i,j) == null) {
                    emptyTiles++;
                }
            }
        }
        return emptyTiles;
    }

    /**
     * Returns the count of a specific token type for a player.
     * @param color the color of the player
     * @param symbol the type of token
     * @return the count of the specified token for the player
     */
    public int getPlayerTokenCount(Color color, Symbol symbol) {
        if (color == Color.PINK) {
            return symbol == Symbol.X ? pink.getX() : pink.getO();
        } else {
            return symbol == Symbol.X ? black.getX() : black.getO();
        }
    }

    /**
     * Adds an observer to the list of observers for this game.
     * @param observer the observer to be added
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers for this game.
     * @param observer the observer to be removed
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of changes to the game state.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

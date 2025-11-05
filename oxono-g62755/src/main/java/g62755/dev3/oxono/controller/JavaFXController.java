package g62755.dev3.oxono.controller;

import g62755.dev3.oxono.model.*;
import g62755.dev3.oxono.view.BlackPlayerInfoView;
import g62755.dev3.oxono.view.GameBoardView;
import g62755.dev3.oxono.view.PinkPlayerInfoView;
import g62755.dev3.util.Observer;

import java.util.Random;

public class JavaFXController implements Observer {

    private Game game;
    private GameBoardView gameBoardView;
    private PinkPlayerInfoView pinkPlayerInfoView;
    private BlackPlayerInfoView blackPlayerInfoView;
    private Position selectedTotem;
    private boolean waitingInsertToken;
    private Symbol lastMovedTotemSymbol;
    private AI ai;

    /**
     * Constructs a new JavaFXController.
     * @param game                The game model.
     * @param gameBoardView       The game board view.
     * @param pinkPlayerInfoView  The pink player info view.
     * @param blackPlayerInfoView The black player info view.
     */
    public JavaFXController(Game game, GameBoardView gameBoardView, PinkPlayerInfoView pinkPlayerInfoView, BlackPlayerInfoView blackPlayerInfoView) {
        this.game = game;
        this.gameBoardView = gameBoardView;
        this.pinkPlayerInfoView = pinkPlayerInfoView;
        this.blackPlayerInfoView = blackPlayerInfoView;
        this.ai = new AI(game, new Random());
        game.addObserver(this);
    }

    /**
     * Updates the player information displayed in the UI.
     */
    public void updatePlayerInfo() {
        int pinkXCount = game.getPlayerTokenCount(Color.PINK, Symbol.X);
        int pinkOCount = game.getPlayerTokenCount(Color.PINK, Symbol.O);
        pinkPlayerInfoView.updateInfo(pinkXCount, pinkOCount);

        int blackXCount = game.getPlayerTokenCount(Color.BLACK, Symbol.X);
        int blackOCount = game.getPlayerTokenCount(Color.BLACK, Symbol.O);
        blackPlayerInfoView.updateInfo(blackXCount, blackOCount);
    }

    /**
     * Returns the pawn at the specified position.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The pawn at the specified position.
     */
    public Pawn getPawnAt(int x, int y) {
        return game.getPawnAt(x, y);
    }

    /**
     * Returns the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return game.getToPlay();
    }

    /**
     * Returns the current game state.
     * @return The current game state.
     */
    public GameState getCurrentGameState() {
        return game.getGameState();
    }

    /**
     * Checks if a move is valid.
     * @param from The starting position.
     * @param to   The ending position.
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(Position from, Position to) {
        return game.isValidMove(from, to);
    }

    /**
     * Checks if a position is within the game board bounds.
     * @param p The position to check.
     * @return true if the position is within bounds, false otherwise.
     */
    public boolean isWithinBounds(Position p) {
        return game.isWithinBounds(p);
    }

    /**
     * Checks if an insertion is valid.
     * @param posTotem The totem position.
     * @param posToken The token position.
     * @return true if the insertion is valid, false otherwise.
     */
    public boolean isValidInsert(Position posTotem, Position posToken) {
        return game.isValidInsert(posTotem, posToken);
    }

    /**
     * Moves the totem to a new position.
     * @param totem The totem to move.
     * @param newPos The new position.
     */
    public void moveTotem(Totem totem, Position newPos) {
        game.move(totem, newPos);
        selectedTotem = newPos;
        lastMovedTotemSymbol = totem.getSymbol();
        waitingInsertToken = true;
    }

    /**
     * Inserts a token at the specified position.
     * @param token    The token to insert.
     * @param posTotem The totem position.
     * @param posToken The token to insert position.
     */
    public void insertToken(Token token, Position posTotem, Position posToken) {
        game.insert(token, posTotem, posToken);
        waitingInsertToken = false;
        selectedTotem = null;
        lastMovedTotemSymbol = null;
    }

    /**
     * Undoes the last action.
     */
    public void undo() {
        game.undo();
        gameBoardView.resetHighlights();
        if (game.getGameState() == GameState.INSERT) {
            waitingInsertToken = true;
            selectedTotem = game.getLastTotemPositionPink();
            lastMovedTotemSymbol = game.getLastSymbolMovedByPink();
        } else if (game.getGameState() == GameState.MOVE) {
            waitingInsertToken = false;
            selectedTotem = null;
            lastMovedTotemSymbol = null;
        }
        updatePlayerInfo();
        gameBoardView.updateCurrentGameStateLabel();
        gameBoardView.updateEmptyTilesLabel();
        gameBoardView.updateCurrentPlayerLabel();
    }

    /**
     * Redoes the last undone action.
     */
    public void redo() {
        game.redo();
        gameBoardView.resetHighlights();
        if (game.getGameState() == GameState.MOVE) {
            waitingInsertToken = true;
            selectedTotem = game.getLastTotemPositionPink();
            lastMovedTotemSymbol = game.getLastSymbolMovedByPink();
        } else if (game.getGameState() == GameState.INSERT) {
            waitingInsertToken = false;
            selectedTotem = null;
            lastMovedTotemSymbol = null;
        }
        updatePlayerInfo();
        gameBoardView.updateCurrentGameStateLabel();
        gameBoardView.updateEmptyTilesLabel();
        gameBoardView.updateCurrentPlayerLabel();
    }

    /**
     * Surrenders the game for the current player.
     */
    public void surrender() {
        if (game.getGameState() != GameState.ENDED) {
            game.surrender();
            gameBoardView.displaySurrenderMessage(game.getToPlay());
            gameBoardView.displaySurrenderModal(game.getToPlay().getC());
        }
    }

    /**
     * Handles a click on a cell in the game board.
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     */
    public void handleClickOnCell(int row, int col) {
        if (game.isEnded()) {
            return;
        }

        Position pos = new Position(row, col);
        Pawn pawn = getPawnAt(row, col);

        if (waitingInsertToken && pawn == null) {
            if (!pos.equals(selectedTotem)) {
                Token newToken = new Token(game.getToPlay().getC(), lastMovedTotemSymbol);
                insertToken(newToken, selectedTotem, pos);
                gameBoardView.updateCurrentPlayerLabel();
                waitingInsertToken = false;
                selectedTotem = null;
                gameBoardView.resetHighlights();
                if (game.won() || game.drew()) {
                    if (game.won()) {
                        if (game.getToPlay().getC() == Color.PINK) {
                            gameBoardView.displayWinMessage(Color.BLACK);
                            gameBoardView.displayWinModal(Color.BLACK);
                        } else {
                            gameBoardView.displayWinMessage(Color.PINK);
                            gameBoardView.displayWinModal(Color.PINK);
                        }
                    } else if (game.drew()) {
                        gameBoardView.displayDrawMessage();
                        gameBoardView.displayDrawModal();
                    }
                    gameBoardView.updateCurrentPlayerLabel();
                    gameBoardView.updateCurrentGameStateLabel();
                } else {
                    aiPlay();
                    aiPlay();
                    gameBoardView.updateCurrentPlayerLabel();
                    gameBoardView.updateCurrentGameStateLabel();
                }
            } else {
                throw new IllegalArgumentException("Invalid insert position.");
            }
        } else if (selectedTotem == null && pawn instanceof Totem) {
            selectedTotem = pos;
            gameBoardView.resetHighlights();
            gameBoardView.highlightMoveOptions(selectedTotem);
        } else if (selectedTotem != null && !waitingInsertToken) {
            if (pawn instanceof Totem) {
                selectedTotem = pos;
                lastMovedTotemSymbol = ((Totem) pawn).getSymbol();
                gameBoardView.resetHighlights();
                gameBoardView.highlightMoveOptions(selectedTotem);
            } else if (!pos.equals(selectedTotem)) { // Don't stay at the same place
                Totem totem = (Totem) game.getPawnAt(selectedTotem.x(), selectedTotem.y());
                moveTotem(totem, pos);
                lastMovedTotemSymbol = totem.getSymbol();
                selectedTotem = pos;
                gameBoardView.resetHighlights();
                gameBoardView.highlightInsertOptions(selectedTotem);
                waitingInsertToken = true;
                gameBoardView.updateCurrentPlayerLabel();
                gameBoardView.updateCurrentGameStateLabel();
            } else {
                throw new IllegalArgumentException("Cannot move totem to the same position.");
            }
        }
    }

    private void aiPlay() {
        if (!game.isEnded()) {
            ai.play();
            if (game.won() || game.drew()) {
                if (game.won()) {
                    if (game.getToPlay().getC() == Color.PINK) {
                        gameBoardView.displayWinMessage(Color.BLACK);
                        gameBoardView.displayWinModal(Color.BLACK);
                    } else {
                        gameBoardView.displayWinMessage(Color.PINK);
                        gameBoardView.displayWinModal(Color.PINK);
                    }
                } else if (game.drew()) {
                    gameBoardView.displayDrawMessage();
                    gameBoardView.displayDrawModal();
                }
            }
        }
    }

    /**
     * Counts the number of empty tiles on the game board.
     * @return The number of empty tiles.
     */
    public int countEmptyTiles() {
        return game.countEmptyTiles();
    }

    /**
     * Updates the game view.
     */
    @Override
    public void update() {
        gameBoardView.displayBoard();
        gameBoardView.updateEmptyTilesLabel();
        updatePlayerInfo();
    }
}
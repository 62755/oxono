package g62755.dev3.oxono.model;

import g62755.dev3.util.Command;

public class InsertCommand implements Command {

    private Board board;
    private Token token;
    private Position posToken;

    /**
     * Creates an InsertCommand with the specified board, token, and target position.
     * @param board the game board
     * @param token the token to be inserted
     * @param posToken the position where the token will be inserted
     */
    public InsertCommand(Board board, Token token, Position posToken) {
        this.board = board;
        this.token = token;
        this.posToken = posToken;
    }

    /**
     * Executes the insertion of the token into the board.
     */
    @Override
    public void execute() {
        board.insertToken(token, posToken);
    }

    /**
     * Reverses the insertion of the token by removing it from the board.
     */
    @Override
    public void unexecute() {
        board.removeToken(posToken);
    }
}

package g62755.dev3.oxono.model;

import g62755.dev3.util.Command;

public class MoveCommand implements Command {

    private Board board;
    private Totem totem;
    private Position from;
    private Position to;

    /**
     * Creates a MoveCommand with the specified board, totem, and positions.
     * @param board the game board
     * @param totem the totem to be moved
     * @param from the starting position of the totem
     * @param to the target position of the totem
     */
    public MoveCommand(Board board, Totem totem, Position from, Position to) {
        this.board = board;
        this.totem = totem;
        this.from = from;
        this.to = to;
    }

    /**
     * Executes the movement of the totem to the target position.
     */
    @Override
    public void execute() {
        board.moveTotem(totem, to);
    }

    /**
     * Reverses the movement of the totem back to its original position.
     */
    @Override
    public void unexecute() {
        board.moveTotem(totem, from);
    }
}

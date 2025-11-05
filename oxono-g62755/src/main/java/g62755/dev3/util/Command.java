package g62755.dev3.util;

public interface Command {

    /**
     * Executes the command.
     * This method should contain the logic to perform the action associated with the command.
     */
    void execute();

    /**
     * Unexecutes the command.
     * This method should contain the logic to undo the action performed by the {@link #execute} method.
     */
    void unexecute();
}

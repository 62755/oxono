package g62755.dev3.util;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    /**
     * Constructs a CommandManager with the provided undo and redo stacks.
     * @param undoStack The stack used to store commands for undoing.
     * @param redoStack The stack used to store commands for redoing.
     */
    public CommandManager(Stack<Command> undoStack, Stack<Command> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    /**
     * Gets the undo stack.
     * @return The stack of commands that can be undone.
     */
    public Stack<Command> getUndoStack() {
        return undoStack;
    }

    /**
     * Gets the redo stack.
     * @return The stack of commands that can be redone.
     */
    public Stack<Command> getRedoStack() {
        return redoStack;
    }

    /**
     * Executes a given command and adds it to the undo stack.
     * Clears the redo stack to prevent inconsistent redo history.
     * @param command The command to be executed.
     */
    public void doIt(Command command) {
        undoStack.add(command);
        redoStack.clear();
    }

    /**
     * Undoes the most recent command by executing its unexecute method.
     * The undone command is then moved to the redo stack.
     */
    public void undo() {
        Command command = undoStack.pop();
        command.unexecute();
        redoStack.add(command);
    }

    /**
     * Redoes the most recent undone command by executing its execute method.
     * The redone command is then moved back to the undo stack.
     */
    public void redo() {
        Command command = redoStack.pop();
        command.execute();
        undoStack.add(command);
    }
}


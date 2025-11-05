package g62755.dev3.oxono.view;

import g62755.dev3.oxono.controller.GameController;
import g62755.dev3.oxono.model.*;

public class GameView {

    private GameController controller;

    /**
     * Sets the game controller for this view.
     * @param controller The game controller.
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Displays the game board.
     */
    public void displayBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Pawn pawn = controller.getPawnAt(i, j);
                if (pawn == null) {
                    System.out.print("_ ");
                } else if (pawn instanceof Totem) {
                    System.out.print("\u001B[34m" + pawn.getSymbol() + "\u001B[0m ");
                } else {
                    if (pawn.getColor() == Color.PINK) {
                        System.out.print("\u001B[35m" + pawn.getSymbol() + "\u001B[0m ");
                    } else if (pawn.getColor() == Color.BLACK) {
                        System.out.print("\u001B[30m" + pawn.getSymbol() + "\u001B[0m ");
                    } else {
                        System.out.print(pawn.getSymbol() + " ");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * Displays the start game message.
     */
    public void displayStartGameMessage() {
        System.out.println("The game starts ! Player pink begins.");
    }

    /**
     * Displays the current player's information.
     * @param toPlay The player whose information is to be displayed.
     */
    public void displayPlayerInformation(Player toPlay) {
        System.out.println("Player " + toPlay.getC() + "'s turn.");
        System.out.println("X tokens left : " + toPlay.getX());
        System.out.println("O tokens left : " + toPlay.getO());
    }

    /**
     * Displays the message when a player surrenders.
     * @param surrenderer The player who surrenders.
     */
    public void displaySurrenderMessage(Player surrenderer) {
        System.out.println("Player " + surrenderer + " gives up !");
        if (surrenderer.getC() == Color.PINK) {
            System.out.println("Player BLACK won !");
        } else {
            System.out.println("Player PINK won !");
        }
        System.out.println("End of the game.");
    }

    /**
     * Displays the win message for the specified color.
     * @param color The color of the winning player.
     */
    public void displayWinMessage(Color color) {
        System.out.println("Player " + color + " won !");
        System.out.println("End of the game.");
    }

    /**
     * Displays the draw message.
     */
    public void displayDrawMessage() {
        System.out.println("Draw !");
        System.out.println("End of the game.");
    }

    /**
     * Displays the prompt message for moving a totem.
     */
    public void displayMoveTotemScannerMessage() {
        System.out.println("Please move a totem. Enter the symbol, and the x and y coordinates (for example : X 0 2) : ");
    }

    /**
     * Displays the prompt message for inserting a token.
     */
    public void displayInsertTokenScannerMessage() {
        System.out.println("Enter the x and y coordinates of the token to insert : ");
    }

    /**
     * Displays the number of empty tiles left.
     * @param emptyTiles The number of empty tiles left.
     */
    public void displayEmptyTiles(int emptyTiles) {
        System.out.println(emptyTiles + " empty tiles left.");
    }
}

package g62755.dev3.oxono.console;

import g62755.dev3.oxono.controller.GameController;
import g62755.dev3.oxono.model.*;
import g62755.dev3.oxono.view.GameView;

public class Main {

    /**
     * The entry point of the application. Initializes the game with a board, players, and starts the game.
     * @param args Command-line arguments (not used in this case).
     */
    public static void main(String[] args) {
        Board board = new Board(6);
        Player pinkPlayer = new Player(Color.PINK, 6);
        Player blackPlayer = new Player(Color.BLACK, 6);
        Game game = new Game(blackPlayer, pinkPlayer, board);
        GameView gameView = new GameView();
        GameController gameController = new GameController(game, gameView);
        gameView.setController(gameController);

        gameController.start();
    }
}

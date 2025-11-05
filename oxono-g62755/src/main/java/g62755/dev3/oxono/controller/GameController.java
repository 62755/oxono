package g62755.dev3.oxono.controller;

import g62755.dev3.oxono.model.*;
import g62755.dev3.oxono.view.GameView;
import g62755.dev3.util.Observer;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController implements Observer {

    private Game game;
    private GameView gameView;
    private AI ai;

    private static final Pattern moveTotemPattern = Pattern.compile("^([XO]) (\\d) (\\d)$");
    private static final Pattern insertTokenPattern = Pattern.compile("^(\\d) (\\d)$");
    private static final Pattern undoPattern = Pattern.compile("^UNDO$");
    private static final Pattern redoPattern = Pattern.compile("^REDO$");
    private static final Pattern surrenderPattern = Pattern.compile("^SURRENDER$");

    /**
     * Constructs a new GameController.
     * @param game     The game model.
     * @param gameView The game view.
     */
    public GameController(Game game, GameView gameView) {
        this.game = game;
        this.gameView = gameView;
        this.ai = new AI(game, new Random());
        game.addObserver(this);
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

    /***
     * Starts the game and handles the game loop.
     */
    public void start() {
        gameView.displayStartGameMessage();
        gameView.displayBoard();

        Scanner scanner = new Scanner(System.in);

        while (!game.won() && !game.drew() && !game.isEnded()) {
            playTurn(scanner);
        }
        if (game.won()) {
            if (game.getToPlay().getC() == Color.PINK) {
                gameView.displayWinMessage(Color.BLACK);
            } else {
                gameView.displayWinMessage(Color.PINK);
            }
        } else if (game.drew()) {
            gameView.displayDrawMessage();
        }
        game.removeObserver(this);
        scanner.close();
    }

    private void playTurn(Scanner scanner) {
        gameView.displayPlayerInformation(game.getToPlay());
        gameView.displayEmptyTiles(game.countEmptyTiles());

        if (game.getToPlay().getC() == Color.BLACK) {
            ai.play();
        } else {
            if (game.getGameState() == GameState.MOVE) {
                moveTotem(scanner);
            } else if (game.getGameState() == GameState.INSERT) {
                insertToken(scanner);
            }
        }
    }

    private void moveTotem(Scanner scanner) {
        gameView.displayMoveTotemScannerMessage();

        String input = scanner.nextLine().trim().toUpperCase();

        if (input.matches(surrenderPattern.pattern())) {
            game.surrender();
            gameView.displaySurrenderMessage(game.getToPlay());
            return;
        }

        if (input.matches(undoPattern.pattern())) {
            game.undo();
            return;
        } else if (input.matches(redoPattern.pattern())) {
            game.redo();
            return;
        }

        Matcher matcher;

        if (input.matches(moveTotemPattern.pattern())) {
            matcher = moveTotemPattern.matcher(input);
            if (matcher.find()) {
                String symbolString = matcher.group(1);
                int x = Integer.parseInt(matcher.group(2));
                int y = Integer.parseInt(matcher.group(3));
                Symbol symbol = Symbol.valueOf(symbolString);

                Position newPosition = new Position(x, y);
                Totem totemToMove = new Totem(Color.BLUE, symbol, newPosition);

                game.move(totemToMove, newPosition);
            } else {
                throw new IllegalArgumentException("Invalid command.");
            }
        } else {
            throw new IllegalArgumentException("Invalid command.");
        }
    }

    private void insertToken(Scanner scanner) {
        gameView.displayInsertTokenScannerMessage();

        String input = scanner.nextLine().trim().toUpperCase();

        if (input.matches(surrenderPattern.pattern())) {
            game.surrender();
            gameView.displaySurrenderMessage(game.getToPlay());
            return;
        }

        if (input.matches(undoPattern.pattern())) {
            game.undo();
            return;
        } else if (input.matches(redoPattern.pattern())) {
            game.redo();
            return;
        }

        Matcher matcher;

        if (input.matches(insertTokenPattern.pattern())) {
            matcher = insertTokenPattern.matcher(input);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));

                Position insertPosition = new Position(x, y);
                Position totemPosition = game.getLastTotemPositionPink();

                Token token = new Token(game.getToPlay().getC(), game.getToInsert());
                game.insert(token, totemPosition, insertPosition);
            } else {
                throw new IllegalArgumentException("Invalid command.");
            }
        } else {
            throw new IllegalArgumentException("Invalid command.");
        }
    }

    /**
     * Updates the game view by redisplaying the game board.
     */
    @Override
    public void update() {
        gameView.displayBoard();
    }
}

package g62755.dev3.oxono.JavaFX;

import g62755.dev3.oxono.controller.JavaFXController;
import g62755.dev3.oxono.model.Board;
import g62755.dev3.oxono.model.Color;
import g62755.dev3.oxono.model.Game;
import g62755.dev3.oxono.model.Player;
import g62755.dev3.oxono.view.BlackPlayerInfoView;
import g62755.dev3.oxono.view.GameBoardView;
import g62755.dev3.oxono.view.MenuView;
import g62755.dev3.oxono.view.PinkPlayerInfoView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private Scene menuScene;
    private Scene gameScene;
    private MenuView menuView;
    private GameBoardView gameBoardView;
    private PinkPlayerInfoView pinkPlayerInfoView;
    private BlackPlayerInfoView blackPlayerInfoView;

    /**
     * Launches the JavaFX application.
     * @param args Command-line arguments (not used in this case).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the primary stage and sets up the menu scene.
     * Displays the menu with options to start the game.
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        menuView = new MenuView();
        menuView.getStartButton().setOnAction(e -> {
            if (menuView.validateInput()) {
                int boardSize = Integer.parseInt(menuView.getBoardSizeInput().getText());
                String difficulty = menuView.getDifficultySelector().getValue();
                startGame(boardSize, difficulty);
            }
        });

        BorderPane menuRoot = new BorderPane();
        menuRoot.setCenter(menuView);
        menuScene = new Scene(menuRoot, 750, 600);

        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Oxono");
        primaryStage.show();
    }

    /**
     * Starts the game by setting up the game board, players, and initializing the necessary views.
     * Switches the scene from the menu to the game board.
     * @param boardSize The size of the game board (e.g., 6x6).
     * @param difficulty The selected difficulty level (not used in this implementation).
     */
    public void startGame(int boardSize, String difficulty) {
        Board board = new Board(boardSize);
        Player playerPink = new Player(Color.PINK, boardSize);
        Player playerBlack = new Player(Color.BLACK, boardSize);

        Game game = new Game(playerBlack, playerPink, board);

        pinkPlayerInfoView = new PinkPlayerInfoView();
        pinkPlayerInfoView.setPrefWidth(150);
        pinkPlayerInfoView.setPadding(new Insets(20));

        blackPlayerInfoView = new BlackPlayerInfoView();
        blackPlayerInfoView.setPrefWidth(150);
        blackPlayerInfoView.setPadding(new Insets(20));

        gameBoardView = new GameBoardView(menuView);
        JavaFXController javaFXController = new JavaFXController(game, gameBoardView, pinkPlayerInfoView, blackPlayerInfoView);
        gameBoardView.setJavaFXController(javaFXController);

        BorderPane gameRoot = new BorderPane();
        gameRoot.setLeft(pinkPlayerInfoView);
        gameRoot.setRight(blackPlayerInfoView);
        gameRoot.setCenter(gameBoardView);

        gameScene = new Scene(gameRoot, 750, 600);

        gameBoardView.displayBoard();
        primaryStage.setScene(gameScene);
    }
}
package g62755.dev3.oxono.view;

import g62755.dev3.oxono.controller.JavaFXController;
import g62755.dev3.oxono.model.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameBoardView extends GridPane {

    private JavaFXController javaFXController;
    private MenuView menuView;
    private Label emptyTilesLabel;
    private Label currentPlayerLabel;
    private Label currentGameStateLabel;

    /**
     * Constructs a new GameBoardView.
     * @param menuView The menu view associated with the game board.
     */
    public GameBoardView(MenuView menuView) {
        this.menuView = menuView;
        this.emptyTilesLabel = new Label();
        this.currentPlayerLabel = new Label("Current Player : ");
        this.currentGameStateLabel = new Label("Current Game State : ");
    }

    /**
     * Sets the JavaFX Controller for this view.
     * @param javaFXController
     */
    public void setJavaFXController(JavaFXController javaFXController) {
        this.javaFXController = javaFXController;
    }

    /**
     * Displays the game board.
     */
    public void displayBoard() {
        this.getChildren().clear();// Clears last game board
        this.setStyle("-fx-background-color: white;");
        this.setAlignment(Pos.CENTER);
        int size = Integer.parseInt(menuView.getBoardSizeInput().getText());

        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button surrenderButton = new Button("Surrender");

        undoButton.setOnAction(event -> {
            javaFXController.undo();
            updateCurrentPlayerLabel();
            updateCurrentGameStateLabel();
        });

        redoButton.setOnAction(event -> {
            javaFXController.redo();
            updateCurrentPlayerLabel();
            updateCurrentGameStateLabel();
        });

        surrenderButton.setOnAction(event -> {
            javaFXController.surrender();
            updateCurrentGameStateLabel();
        });

        HBox buttonBox = new HBox(20, undoButton, redoButton, surrenderButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-padding: 20 0 20 0;");

        this.add(buttonBox, 0, 0, size, 1);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle(50, 50);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.WHITE);

                Pawn pawn = javaFXController.getPawnAt(i, j);
                if (pawn != null) {
                    if (pawn instanceof Totem) {
                        rectangle.setFill(Color.BLUE);
                    } else if (pawn.getColor() == g62755.dev3.oxono.model.Color.PINK){
                        rectangle.setFill(Color.PINK);
                    } else {
                        rectangle.setFill(Color.BLACK);
                    }
                    Text text = new Text(pawn.getSymbol().toString());
                    text.setFill(Color.WHITE);
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    stackPane.getChildren().addAll(rectangle, text); // Add rectangle and text to StackPane
                } else {
                    stackPane.getChildren().add(rectangle); // Add only rectangle if no pawn
                }
                int finalI = i;
                int finalJ = j;
                stackPane.setOnMouseClicked(event -> javaFXController.handleClickOnCell(finalI, finalJ));
                this.add(stackPane, j, i + 1); // Add StackPane to GridPane
            }
        }

        emptyTilesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        emptyTilesLabel.setStyle("-fx-padding: 20 0 0 0;");
        this.add(emptyTilesLabel, 0, size + 1, size, 1);

        currentGameStateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        currentGameStateLabel.setStyle("-fx-padding: 10 0 10 0;");
        this.add(currentGameStateLabel, 0, size + 2, size, 1);

        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        currentPlayerLabel.setStyle("-fx-padding: 10 0 10 0;");
        this.add(currentPlayerLabel, 0, size + 3, size, 1);;
    }

    /**
     * Highlights valid move options for the currently selected pawn.
     * @param pos The position of the selected pawn.
     */
    public void highlightMoveOptions(Position pos) {
        for (Node node : this.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                int colIndex = GridPane.getColumnIndex(stackPane) != null ? GridPane.getColumnIndex(stackPane) : 0;
                int rowIndex = GridPane.getRowIndex(stackPane) != null ? GridPane.getRowIndex(stackPane) : 0;
                Position newPos = new Position(rowIndex - 1, colIndex);

                if (javaFXController.isWithinBounds(newPos) && javaFXController.isValidMove(pos, newPos)) {
                    Rectangle rectangle = (Rectangle) stackPane.getChildren().get(0);
                    rectangle.setFill(Color.GREEN);
                }
            }
        }
    }

    /**
     * Highlights valid insertion options for the Totem.
     * @param posTotem The position of the Totem to be inserted.
     */
    public void highlightInsertOptions(Position posTotem) {
        for (Node node : this.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                int colIndex = GridPane.getColumnIndex(stackPane) != null ? GridPane.getColumnIndex(stackPane) : 0;
                int rowIndex = GridPane.getRowIndex(stackPane) != null ? GridPane.getRowIndex(stackPane) : 0;
                Position newPos = new Position(rowIndex - 1, colIndex);

                if (javaFXController.isWithinBounds(newPos) && javaFXController.isValidInsert(posTotem, newPos)) {
                    Rectangle rectangle = (Rectangle) stackPane.getChildren().get(0);
                    rectangle.setFill(Color.GREEN);
                }
            }
        }
    }

    /**
     * Resets the highlights on the game board.
     */
    public void resetHighlights() {
        for (Node node : this.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                Rectangle rectangle = (Rectangle) stackPane.getChildren().get(0);
                int rowIndex = (GridPane.getRowIndex(stackPane) != null) ? GridPane.getRowIndex(stackPane) - 1 : -1;
                int colIndex = (GridPane.getColumnIndex(stackPane) != null) ? GridPane.getColumnIndex(stackPane) : -1;

                if (stackPane.getChildren().size() == 1) {
                    rectangle.setFill(Color.WHITE);
                } else {
                    Pawn pawn = javaFXController.getPawnAt(rowIndex, colIndex);
                    if (pawn == null) {
                        rectangle.setFill(Color.WHITE);
                    } else if (pawn instanceof Totem) {
                        rectangle.setFill(Color.BLUE);
                    } else if (pawn.getColor() == g62755.dev3.oxono.model.Color.PINK) {
                        rectangle.setFill(Color.PINK);
                    } else {
                        rectangle.setFill(Color.BLACK);
                    }
                }
            }
        }
    }

    /**
     * Updates the label displaying the number of empty tiles left on the board.
     */
    public void updateEmptyTilesLabel() {
        int emptyTiles = javaFXController.countEmptyTiles();
        emptyTilesLabel.setText(emptyTiles + " empty tiles left.");
    }

    /**
     * Updates the label displaying the current player (PINK or BLACK).
     */
    public void updateCurrentPlayerLabel() {
        Player currentPlayer = javaFXController.getCurrentPlayer();
        String playerName = currentPlayer.getC() == g62755.dev3.oxono.model.Color.PINK ? "PINK" : "BLACK";
        if (javaFXController.getCurrentGameState() == GameState.ENDED) {
            playerName = "";
        }
        currentPlayerLabel.setText("Current Player : " + playerName);
    }

    /**
     * Updates the label displaying the current game state (MOVE, INSERT, or ENDED).
     */
    public void updateCurrentGameStateLabel() {
        GameState currentGameState = javaFXController.getCurrentGameState();
        String gameState;
        if (currentGameState == GameState.MOVE) {
            gameState = "MOVE";
        } else if (currentGameState == GameState.INSERT) {
            gameState = "INSERT";
        } else {
            gameState = "ENDED";
        }
        currentGameStateLabel.setText("Current Game State : " + gameState);
    }

    /**
     * Displays a message in the console when a player surrenders.
     * @param surrenderer The player who surrenders.
     */
    public void displaySurrenderMessage(Player surrenderer) {
        System.out.println("Player " + surrenderer + " gives up !");
        if (surrenderer.getC() == g62755.dev3.oxono.model.Color.PINK) {
            System.out.println("Player BLACK won !");
        } else {
            System.out.println("Player PINK won !");
        }
        System.out.println("End of the game.");
    }

    /**
     * Displays a message in the console when a player wins.
     * @param color The color of the winning player.
     */
    public void displayWinMessage(g62755.dev3.oxono.model.Color color) {
        System.out.println("Player " + color + " won !");
        System.out.println("End of the game.");
    }

    /**
     * Displays a message in the console when the game ends in a draw.
     */
    public void displayDrawMessage() {
        System.out.println("Draw !");
        System.out.println("End of the game.");
    }

    /**
     * Displays a modal showing the winner of the game.
     * @param winnerColor The color of the winning player.
     */
    public void displayWinModal(g62755.dev3.oxono.model.Color winnerColor) {
        Stage winStage = new Stage();
        winStage.initModality(Modality.APPLICATION_MODAL);
        winStage.setTitle("Game Over");

        String winner = winnerColor == g62755.dev3.oxono.model.Color.PINK ? "PINK" : "BLACK";
        Label winLabel = new Label("Player " + winner + " wins !");
        winLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> winStage.close());

        VBox layout = new VBox(10, winLabel, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 300, 150);
        winStage.setScene(scene);
        winStage.showAndWait();
    }

    /**
     * Displays a modal showing that the game ended in a draw.
     */
    public void displayDrawModal() {
        Stage drawStage = new Stage();
        drawStage.initModality(Modality.APPLICATION_MODAL);
        drawStage.setTitle("Game Over");

        Label drawLabel = new Label("Draw !");
        drawLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> drawStage.close());

        VBox layout = new VBox(10, drawLabel, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 300, 150);
        drawStage.setScene(scene);
        drawStage.showAndWait();
    }

    /**
     * Displays a modal showing the surrender message and the winner of the game.
     * @param surrenderColor The color of the player who surrendered.
     */
    public void displaySurrenderModal(g62755.dev3.oxono.model.Color surrenderColor) {
        Stage surrenderStage = new Stage();
        surrenderStage.initModality(Modality.APPLICATION_MODAL);
        surrenderStage.setTitle("Game Over");

        String surrenderer = surrenderColor == g62755.dev3.oxono.model.Color.PINK ? "PINK" : "BLACK";
        String winner = surrenderColor == g62755.dev3.oxono.model.Color.PINK ? "BLACK" : "PINK";
        Label surrenderLabel = new Label("Player " + surrenderer + " gives up ! Player " + winner + " wins !");
        surrenderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> surrenderStage.close());

        VBox layout = new VBox(10, surrenderLabel, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 500, 150);
        surrenderStage.setScene(scene);
        surrenderStage.showAndWait();
    }

}
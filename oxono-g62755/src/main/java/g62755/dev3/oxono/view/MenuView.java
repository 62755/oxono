package g62755.dev3.oxono.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MenuView extends VBox {

    private TextField boardSizeInput;
    private ComboBox<String> difficultySelector;
    private Button startButton;

    /**
     * Constructs a new MenuView.
     */
    public MenuView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        Label boardSizeLabel = new Label("Enter the board size (must be an even number) : ");
        boardSizeInput = new TextField();
        boardSizeInput.setMaxWidth(50);

        Label difficultyLabel = new Label("Select the difficulty level :");
        difficultySelector = new ComboBox<>();
        difficultySelector.getItems().addAll("Easy", "Medium", "Hard");
        difficultySelector.setValue("Easy"); // Default value

        startButton = new Button("Start the game");
        this.getChildren().addAll(boardSizeLabel, boardSizeInput, difficultyLabel, difficultySelector, startButton);
        startButton.setOnAction(e -> validateInput());
    }

    /**
     * Validates the input for board size and difficulty level.
     * @return true if the input is valid, false otherwise.
     */
    public boolean validateInput() {
        String boardSizeText = boardSizeInput.getText();
        if (!isNumber(boardSizeText)) {
            displayError("Invalid board size. Please enter a valid number.");
            return false;
        }

        int boardSize = Integer.parseInt(boardSizeText);
        if (boardSize % 2 != 0) {
            displayError("The board size must be an even number.");
            return false;
        }

        String difficulty = difficultySelector.getValue();
        if (!"Easy".equals(difficulty)) {
            displayError("Only Easy difficulty level is available.");
            return false;
        }

        return true;
    }

    /**
     * Returns the TextField for board size input.
     * @return The TextField for board size input.
     */
    public TextField getBoardSizeInput() {
        return boardSizeInput;
    }

    /**
     * Returns the ComboBox for difficulty selection.
     * @return The ComboBox for difficulty selection.
     */
    public ComboBox<String> getDifficultySelector() {
        return difficultySelector;
    }

    /**
     * Returns the Button to start the game.
     * @return The Button to start the game.
     */
    public Button getStartButton() {
        return startButton;
    }

    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isNumber(String str) {
        return str.matches("\\d+");
    }
}

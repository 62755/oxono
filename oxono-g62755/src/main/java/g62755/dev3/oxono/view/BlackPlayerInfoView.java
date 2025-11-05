package g62755.dev3.oxono.view;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class BlackPlayerInfoView extends GridPane {
    private Label playerLabel;
    private Label tokenXLabel;
    private Label tokenOLabel;

    /**
     * Constructs a new BlackPlayerInfoView.
     */
    public BlackPlayerInfoView() {
        playerLabel = new Label("Player BLACK :");
        tokenXLabel = new Label("X Tokens Left : ");
        tokenOLabel = new Label("O Tokens Left : ");

        this.add(playerLabel, 0, 0);
        this.add(tokenXLabel, 0, 1);
        this.add(tokenOLabel, 0, 2);
    }

    /**
     * Updates the information about the Black player's tokens.
     * @param xTokens The number of X tokens left.
     * @param oTokens The number of O tokens left.
     */
    public void updateInfo(int xTokens, int oTokens) {
        tokenXLabel.setText("X Tokens Left : " + xTokens);
        tokenOLabel.setText("O Tokens Left : " + oTokens);
    }
}
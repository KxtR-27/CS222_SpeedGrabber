package speedgrabber.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import speedgrabber.ApiDataGrabber;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;

import java.io.IOException;
import java.util.List;

public class SpeedGrabberController {

    private Game game;
    @FXML
    private Label gameLabel;
    @FXML
    private TextField gameSearchField;
    @FXML
    private Button gameSearchButton;

    private List<Category> categories;
    @FXML
    private ChoiceBox categoryDropdown;

    private Leaderboard leaderboard;
    @FXML
    private TextArea leaderboardArea;

    public void searchGame() throws IOException {
        game = ApiDataGrabber.getGame(gameSearchField.getText());
        gameLabel.setText(String.format("Game found: %s", game.name()));

        categories = ApiDataGrabber.getCategories(game);
        for (Category category : categories)
            categoryDropdown.getItems().add(category);
    }
    public void showCategoryLeaderboard() throws IOException {
        if (categoryDropdown.getValue().equals(""))
            return;

        leaderboard = ApiDataGrabber.getLeaderboard((Category) categoryDropdown.getValue());
        StringBuilder leaderboardBuilder = new StringBuilder();
        for (String runID : leaderboard.runIDs())
            leaderboardBuilder.append(String.format("%s%n", runID));

        leaderboardArea.setText(leaderboardBuilder.toString());
    }
}

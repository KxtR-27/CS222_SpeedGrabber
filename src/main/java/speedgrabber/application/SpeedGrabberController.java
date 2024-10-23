package speedgrabber.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import speedgrabber.ApiDataGrabber;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused"})
public class SpeedGrabberController {

    @FXML
    private Label gameLabel;
    @FXML
    private TextField gameSearchField;
    @FXML
    private Button gameSearchButton;

    @FXML
    private ChoiceBox categoryDropdown;

    @FXML
    private TextArea leaderboardArea;

    public void searchGame() throws IOException {
        categoryDropdown.getItems().clear();

        leaderboardArea.setText("");
        gameSearchField.setDisable(true);

        Game game = ApiDataGrabber.getGame(gameSearchField.getText());
        gameLabel.setText(String.format("Game found: %s", game.name()));

        List<Category> categories = ApiDataGrabber.getCategories(game);
        for (Category category : categories)
            //noinspection unchecked
            categoryDropdown.getItems().add(category);

        gameSearchField.setText("");
        gameSearchField.setDisable(false);
    }
    public void showCategoryLeaderboard() throws IOException {
        if (categoryDropdown.getValue().equals(""))
            return;

        Leaderboard leaderboard = ApiDataGrabber.getLeaderboard((Category) categoryDropdown.getValue(), 20);
        StringBuilder leaderboardBuilder = new StringBuilder();
        for (Map.Entry<Integer, Run> entry : leaderboard.runs().entrySet())
            leaderboardBuilder.append(String.format(
                    "%d.\t%s%n",
                    entry.getKey(),
                    entry.getValue()));

        leaderboardArea.setText(leaderboardBuilder.toString());
    }
}

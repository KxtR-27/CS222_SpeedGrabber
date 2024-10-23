package speedgrabber.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import speedgrabber.ApiDataGrabber;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import javax.naming.NameNotFoundException;
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
    private ChoiceBox<Category> categoryDropdown;

    @FXML
    private TextArea leaderboardArea;

    public void searchGame() {
        try {
            if (gameSearchField.getText().isEmpty())
                throw new NameNotFoundException("Please enter a game abbreviation or ID.");

            categoryDropdown.getItems().clear();

            leaderboardArea.setText("");
            gameSearchField.setDisable(true);

            Game game = ApiDataGrabber.getGame(gameSearchField.getText());
            gameLabel.setText(String.format("Game found: %s", game.name()));

            List<Category> categories = ApiDataGrabber.getCategories(game);
            for (Category category : categories)
                categoryDropdown.getItems().add(category);
        }
        catch (Exception e) {
            showErrorDialog(e);
        }
        finally {
            gameSearchField.setText("");
            gameSearchField.setDisable(false);
        }
    }
    public void showCategoryLeaderboard() {
        try {
            if (categoryDropdown.getValue() == null)
                throw new NullPointerException("Category dropdown is empty. Please select a category first.");

            Leaderboard leaderboard = ApiDataGrabber.getLeaderboard(categoryDropdown.getValue(), 20);
            StringBuilder leaderboardBuilder = new StringBuilder();
            for (Map.Entry<Integer, Run> entry : leaderboard.runs().entrySet())
                leaderboardBuilder.append(String.format(
                        "%d.\t%s%n",
                        entry.getKey(),
                        entry.getValue()));

            leaderboardArea.setText(leaderboardBuilder.toString());
        }
        catch (Exception e) {
            showErrorDialog(e);
        }
    }

    private void showErrorDialog(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String exceptionName = e.getClass().getSimpleName();
        String exceptionDetails = e.getMessage();

        alert.setTitle("Error");
        alert.setHeaderText(exceptionName);
        alert.setContentText(exceptionDetails);
        alert.showAndWait();
    }
}

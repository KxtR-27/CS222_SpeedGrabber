package speedgrabber.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import speedgrabber.ApiDataGrabber;
import speedgrabber.records.*;
import speedgrabber.records.interfaces.Player;

import javax.naming.NameNotFoundException;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.List;

@SuppressWarnings({"unused"})
public class SpeedGrabberController {
    Game activeGame;
    List<Level> activeLevels;
    List<Category> activeCategories;

    @FXML
    private Label gameLabel;
    @FXML
    private ChoiceBox<Category> categoryDropdown;
    @FXML
    private ChoiceBox<Level> levelDropdown;
    @FXML
    private CheckBox showLevelsCheckbox;
    @FXML
    private Button leaderboardButton;

    @FXML
    private TextField gameSearchField;
    @FXML
    private Button gameSearchButton;
    @FXML
    private Button clearAllButton;

    @FXML
    private Slider maxRunsSlider;

    @FXML
    private TextArea leaderboardArea;

    // Button/Event Actions
    public void searchGame() {
        try {
            if (gameSearchField.getText().isEmpty())
                throw new NameNotFoundException("Please enter a game abbreviation or ID.");

            levelDropdown.getItems().clear();
            categoryDropdown.getItems().clear();

            leaderboardArea.setText("");
            gameSearchField.setDisable(true);

            activeGame = ApiDataGrabber.getGame(gameSearchField.getText());
            activeLevels = ApiDataGrabber.getListOfLevels(activeGame);
            activeCategories = ApiDataGrabber.getListOfCategories(activeGame);

            levelDropdown.getItems().clear();
            for (Level level : activeLevels)
                levelDropdown.getItems().add(level);

            gameLabel.setText(String.format("Game found:%n%s", activeGame.name()));
            checkLevels();
        }
        catch (UnknownHostException e) {
            AppAlerts.showGenericError(new UnknownHostException("A network error occurred. Please check your internet connection"));
        }
        catch (FileNotFoundException e) {
            AppAlerts.showSearchError(new FileNotFoundException(gameSearchField.getText()));
        }
        catch (Exception e) {
            AppAlerts.showGenericError(e);
        }
        finally {
            gameSearchField.setText("");
            gameSearchField.setDisable(false);
        }
    }
    public void checkLevels() {
        if (showLevelsCheckbox.isSelected()) {
            levelDropdown.setDisable(false);

            categoryDropdown.getItems().clear();
            for (Category category : activeCategories)
                if (category.type().equals("per-level"))
                    categoryDropdown.getItems().add(category);
        }
        else {
            levelDropdown.setDisable(true);

            categoryDropdown.getItems().clear();
            for (Category category : activeCategories)
                if (category.type().equals("per-game"))
                    categoryDropdown.getItems().add(category);
        }
    }

    public void searchLeaderboard() {
        try {
            if (levelDropdown.getValue() == null && showLevelsCheckbox.isSelected())
                throw new NullPointerException("Level dropdown is empty. Please select a level first.");
            if (categoryDropdown.getValue() == null)
                throw new NullPointerException("Category dropdown is empty. Please select a category first.");

            Leaderboard leaderboard;
            int maxRuns = (int) maxRunsSlider.getValue();

            if (showLevelsCheckbox.isSelected())
                leaderboard = ApiDataGrabber.getLeaderboard(levelDropdown.getValue(), categoryDropdown.getValue(), maxRuns);
            else
                leaderboard = ApiDataGrabber.getLeaderboard(categoryDropdown.getValue(), maxRuns);

            List<Run> leaderboardRuns = ApiDataGrabber.getListOfRuns(leaderboard, maxRuns);
            List<Player[]> leaderboardPlayers = ApiDataGrabber.getPlayersInRuns(leaderboard, maxRuns);

            leaderboardArea.setText(AppUtils.formatLeaderboard(leaderboardRuns, leaderboardPlayers, maxRuns));
        }
        catch (Exception e) {
            AppAlerts.showGenericError(e);
        }
    }

    public void clearAllFields() {
        activeGame = null;
        activeLevels = List.of();
        activeCategories = List.of();

        gameLabel.setText(String.format("%nNo Game Active"));
        gameSearchField.setText("");

        levelDropdown.getItems().clear();
        categoryDropdown.getItems().clear();
        maxRunsSlider.setValue(0);

        leaderboardArea.setText("");
    }

}

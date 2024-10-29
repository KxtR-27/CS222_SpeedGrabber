package speedgrabber.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import speedgrabber.ApiDataGrabber;
import speedgrabber.records.*;
import speedgrabber.records.interfaces.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    private CheckBox levelsCheckbox;
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
                throw new NullPointerException("Please enter a game abbreviation or ID.");

            activeGame = ApiDataGrabber.getGame(gameSearchField.getText());
            gameLabel.setText(activeGame.name());
            leaderboardArea.clear();

            gameSearchField.setDisable(true);

            levelDropdown.getItems().clear();
            activeLevels = ApiDataGrabber.getListOfLevels(activeGame);
            for (Level level : activeLevels)
                levelDropdown.getItems().add(level);
            if (!activeLevels.isEmpty())
                levelDropdown.setValue(levelDropdown.getItems().getFirst());

            categoryDropdown.getItems().clear();
            activeCategories = ApiDataGrabber.getListOfCategories(activeGame);

            gameSearchField.setDisable(false);

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
        categoryDropdown.getItems().clear();
        levelDropdown.setDisable(!levelsCheckbox.isSelected());

        if (levelsCheckbox.isSelected()) {
            for (Category category : activeCategories)
                if (category.type().equals("per-level"))
                    categoryDropdown.getItems().add(category);
        }
        else {
            for (Category category : activeCategories)
                if (category.type().equals("per-game"))
                    categoryDropdown.getItems().add(category);
        }

        categoryDropdown.setValue(categoryDropdown.getItems().getFirst());
    }

    public void searchLeaderboard() {
        leaderboardArea.setText("");

        try {
            Leaderboard leaderboard = getLeaderboardWithContext();
            int maxRuns = (int) maxRunsSlider.getValue();

            List<Run> leaderboardRuns = ApiDataGrabber.getListOfRuns(leaderboard, maxRuns);
            List<Player[]> leaderboardPlayers = ApiDataGrabber.getPlayersInRuns(leaderboard, maxRuns);

            leaderboardArea.setText(AppUtils.formatLeaderboard(leaderboardRuns, leaderboardPlayers, maxRuns));
        }
        catch (Exception e) {
            AppAlerts.showGenericError(e);
        }
    }
    public Leaderboard getLeaderboardWithContext() throws IOException {
        if (activeGame == null)
            throw new NullPointerException("No game selected.");

        String url = String.format("https://www.speedrun.com/api/v1/leaderboards/%s/", activeGame.id());

        Level levelSelection = levelDropdown.getValue();
        Category categorySelection = categoryDropdown.getValue();
        int maxRuns = (int) maxRunsSlider.getValue();

        boolean levelMatters = levelSelection != null && levelsCheckbox.isSelected();
        boolean categoryMatters = categorySelection != null;

        if (!categoryMatters)
            throw new NullPointerException("Please select a category from the dropdown.");

        if (levelMatters)
            url += String.format("level/%s/%s", levelSelection.id(), categorySelection.id());
        else
            url += String.format("category/%s", categorySelection.id());

        return ApiDataGrabber.getLeaderboard(url, maxRuns);
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

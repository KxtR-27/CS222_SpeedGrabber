package speedgrabber.application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import speedgrabber.ApiDataGrabber;
import speedgrabber.SGUtils;
import speedgrabber.records.*;

import javax.naming.NameNotFoundException;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
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
            showErrorDialog(new UnknownHostException("A network error occurred. Please check your internet connection"));
        }
        catch (FileNotFoundException e) {
            showErrorDialog(new FileNotFoundException(gameSearchField.getText()));
        }
        catch (Exception e) {
            showErrorDialog(e);
        }
        finally {
            gameSearchField.setText("");
            gameSearchField.setDisable(false);
        }
    }
    public void showLeaderboard() {
        try {
            if (levelDropdown.getValue() == null && showLevelsCheckbox.isSelected())
                throw new NullPointerException("Level dropdown is empty. Please select a level first.");
            if (categoryDropdown.getValue() == null)
                throw new NullPointerException("Category dropdown is empty. Please select a category first.");

            Leaderboard leaderboard;

            if (showLevelsCheckbox.isSelected())
                leaderboard = ApiDataGrabber.getLeaderboard(levelDropdown.getValue(), categoryDropdown.getValue(), (int) maxRunsSlider.getValue());
            else
                leaderboard = ApiDataGrabber.getLeaderboard(categoryDropdown.getValue(), (int) maxRunsSlider.getValue());

            List<Run> leaderboardRuns = ApiDataGrabber.getListOfRuns(leaderboard, (int) maxRunsSlider.getValue());

            StringBuilder leaderboardBuilder = new StringBuilder(String.format(
                    "%-3s %-25s %-25s %s%n",
                    "#", "Player", "Time", "Date"
            ));
            for (int i = 0; i < maxRunsSlider.getValue() && i < leaderboardRuns.size(); i++)
                leaderboardBuilder.append(String.format("%s%n", leaderboardRuns.get(i)));

            leaderboardArea.setText(leaderboardBuilder.toString());
        }
        catch (Exception e) {
            showErrorDialog(e);
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

    // Utility Methods
    private void showErrorDialog(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String exceptionName = e.getClass().getSimpleName();
        String exceptionDetails = e.getMessage();
        ObservableList<ButtonType> buttonTypes = alert.getButtonTypes();

        e.printStackTrace(System.err);

        if (e instanceof FileNotFoundException) {
            buttonTypes.add(new ButtonType("Search", ButtonBar.ButtonData.HELP));
            exceptionDetails = String.format("Whoops! We couldn't find a game by the slug \"%s\". Press 'Search' to look for it online.", e.getMessage());
        }

        alert.setTitle("Error");
        alert.setHeaderText(exceptionName);
        alert.setContentText(exceptionDetails);
        alert.showAndWait();

        if (e instanceof FileNotFoundException && alert.getResult().equals(buttonTypes.get(1))) {
            try {
                openLink(URI.create(String.format(
                        "https://www.speedrun.com/search?q=%s",
                        SGUtils.encodeForSearchResults(e.getMessage())
                )));
            } catch (IOException uriError) {
                showErrorDialog(new IOException("There was a problem opening the link."));
            }
        }
    }
    private void openLink(URI uri) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri);
        }
    }
}

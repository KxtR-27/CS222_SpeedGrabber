package speedgrabber.application;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import speedgrabber.SGUtils;
import speedgrabber.apidatagrabbers.*;
import speedgrabber.records.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused"})
public class SpeedGrabberController {
    Game activeGame;
    List<Level> activeLevels;
    List<Category> activeCategories;
    ObservableList<TableRun> tableRuns;

    @FXML private Label gameNameLabel;
    @FXML private TextField gameSearchField;

    @FXML private ChoiceBox<Category> categoryDropdown;
    @FXML private Label levelIndicator;

    @FXML private ChoiceBox<Level> levelDropdown;
    @FXML private CheckBox levelBox;

    @FXML private Button leaderboardButton;
    @FXML private Label runsLabel;
    @FXML private Spinner<Integer> runsSpinner;

    @FXML private TableView<TableRun> leaderboardTable;
    @FXML private TableColumn<TableRun, Integer> placeColumn;
    @FXML private TableColumn<TableRun, String> playerColumn;
    @FXML private TableColumn<TableRun, String> timeColumn;
    @FXML private TableColumn<TableRun, String> dateColumn;

    @FXML private Button clearButton;

    // Startup Values
    public void initialize() {
        runsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 10));

        tableRuns = FXCollections.observableList(new ArrayList<>());
        leaderboardTable.setItems(tableRuns);

        placeColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPlace()));
        playerColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPlayersName()));
        timeColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getRunTime().toString()));
        dateColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDateAgo()));
    }

    // Button/Event Actions
    public void searchGame() {
        String searchText = gameSearchField.getText().trim();

        if (searchText.isEmpty())
            return;

        String encodedSlug = SGUtils.encodeSlug(searchText);

        try {
            gameSearchField.setDisable(true);

            activeGame = GameGrabber.grab(encodedSlug);
            gameNameLabel.setText(activeGame.name());
            gameNameLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 13));
            gameNameLabel.setDisable(false);

            activeCategories = CategoryGrabber.grabList(activeGame);
            categoryDropdown.setDisable(false);

            activeLevels = LevelGrabber.grabList(activeGame);
            levelDropdown.getItems().setAll(activeLevels);
            if (!activeLevels.isEmpty()) levelDropdown.setValue(levelDropdown.getItems().getFirst());
            levelBox.setDisable(false);

            configureDropdowns();

            leaderboardButton.setDisable(false);
            runsLabel.setDisable(false);
            runsSpinner.setDisable(false);

            gameSearchField.setDisable(false);
            leaderboardTable.setDisable(true);
        }
        catch (UnknownHostException e) {
            AppDialogs.showGenericError(new UnknownHostException("A network error occurred. Please check your internet connection"));
        }
        catch (FileNotFoundException e) {
            AppDialogs.showSearchError(new FileNotFoundException(encodedSlug));
        }
        catch (Exception e) {
            AppDialogs.showGenericError(e);
        }
        finally {
            gameSearchField.setText("");
            gameSearchField.setDisable(false);
        }
    }
    public void configureDropdowns() {
        boolean shouldShowLevels = levelBox.isSelected();
        levelDropdown.setDisable(!shouldShowLevels);
        levelIndicator.setVisible(shouldShowLevels);
        categoryDropdown.getItems().clear();

        for (Category activeCategory : activeCategories) {
            if (shouldShowLevels && activeCategory.type().equals("per-level"))
                categoryDropdown.getItems().add(activeCategory);
            else if (!shouldShowLevels && activeCategory.type().equals("per-game"))
                categoryDropdown.getItems().add(activeCategory);
        }

        categoryDropdown.setValue(categoryDropdown.getItems().getFirst());
    }

    public void getLeaderboardTableData() {
        gameSearchField.setDisable(true);

        try {
            Leaderboard leaderboard = getLeaderboardWithContext();
            if (leaderboard == null)
                return;

            List<Run> runs = RunGrabber.grabList(getLeaderboardWithContext(), runsSpinner.getValue());
            leaderboardTable.getItems().clear();
            for (Run run : runs)
                tableRuns.add(new TableRun(run, PlayerGrabber.grabArrayFromRun(run)));
            leaderboardTable.setItems(FXCollections.observableList(tableRuns));
            leaderboardTable.setDisable(false);
        }
        catch (IOException e) {
            AppDialogs.showGenericError(new IOException("Something went wrong while grabbing leaderboard data :("));
        }
        catch (Exception e) {
            AppDialogs.showGenericError(e);
        }

        gameSearchField.setDisable(false);
    }
    private Leaderboard getLeaderboardWithContext() {
        String leaderboardLink =
                String.format("https://www.speedrun.com/api/v1/leaderboards/%s/", activeGame.id()) +
                        ((levelBox.isSelected())
                        ? String.format("level/%s/%s", levelDropdown.getValue().id(), categoryDropdown.getValue().id())
                        : String.format("category/%s", categoryDropdown.getValue().id())
                );

        try {
            return LeaderboardGrabber.grab(leaderboardLink, runsSpinner.getValue());
        }
        catch (IOException e) {
            AppDialogs.showGenericError(new IOException("Something went wrong with the leaderboard. :("));
        }
        return null;
    }

    public void clearAllFields() {
        activeGame = null;
        activeCategories.clear();
        activeLevels.clear();

        gameNameLabel.setText("No Game Selected");
        gameNameLabel.setFont(Font.font("System", FontWeight.LIGHT, FontPosture.ITALIC, 13));
        gameNameLabel.setDisable(true);
        gameSearchField.setText("");

        categoryDropdown.getItems().clear();
        categoryDropdown.setDisable(true);

        levelDropdown.getItems().clear();
        levelDropdown.getItems().clear();
        levelBox.setSelected(false);
        levelBox.setDisable(true);

        leaderboardButton.setDisable(true);
        runsLabel.setDisable(true);
        runsSpinner.setDisable(true);

        leaderboardTable.getItems().clear();
        leaderboardTable.setDisable(true);
    }
}

package speedgrabber.application;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import speedgrabber.SGUtils;

import java.io.IOException;
import java.net.URI;

// Class and methods are intentionally package-private
class AppAlerts {
    static Alert getGenericError(Exception e) {
        e.printStackTrace(System.err);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        String exceptionName = e.getClass().getSimpleName();
        String exceptionDetails = e.getMessage();

        alert.setTitle("Error");
        alert.setHeaderText(exceptionName);
        alert.setContentText(exceptionDetails);

        return alert;
    }
    static void showGenericError(Exception e) {
        getGenericError(e).showAndWait();
    }

    static void showSearchError(Exception e) {
        Alert searchAlert = getGenericError(e);
        ObservableList<ButtonType> buttonTypes = searchAlert.getButtonTypes();

        buttonTypes.add(new ButtonType("Search", ButtonBar.ButtonData.HELP));
        searchAlert.setHeaderText("No game found :(");
        searchAlert.setContentText(String.format("Whoops! We couldn't find a game by the slug \"%s\". Press 'Search' to look for it online.", e.getMessage()));

        searchAlert.showAndWait();

        if (searchAlert.getResult().equals(buttonTypes.get(1))) {
            try {
                SGUtils.openLink(URI.create(String.format(
                        "https://www.speedrun.com/search?q=%s",
                        e.getMessage()
                )));
            } catch (IOException uriError) {
                showGenericError(new IOException("There was a problem opening the link."));
            }
        }
    }
}

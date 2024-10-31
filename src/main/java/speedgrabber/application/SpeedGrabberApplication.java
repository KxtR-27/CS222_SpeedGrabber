package speedgrabber.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SpeedGrabberApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpeedGrabberApplication.class.getResource("speedgrabber-view.fxml"));



        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Speed Grabber v0.1a");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

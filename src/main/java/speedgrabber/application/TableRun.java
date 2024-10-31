package speedgrabber.application;

import javafx.beans.property.*;
import speedgrabber.SGUtils;
import speedgrabber.records.Run;
import speedgrabber.records.interfaces.Player;

import java.time.LocalTime;

public class TableRun {
    private final Run run;
    private final Player[] players;

    public TableRun(Run run, Player[] players) {
        this.run = run;
        this.players = players;

        placeProperty().set(run.place());
        playersProperty().set((players.length == 1) ? players[0].playername() : players[0].playername() + " [+]");
        runTimeProperty().set(run.primaryTime());
        dateProperty().set(SGUtils.asDateAgo(run.dateOfRun()));
    }

    public Run getRun() {
        return run;
    }
    // Not Yet Implemented
    @SuppressWarnings("unused")
    public Player[] getPlayers() {
        return players;
    }

    private IntegerProperty place;
    public IntegerProperty placeProperty() {
        if (place == null) place = new SimpleIntegerProperty(this, "place");
        return place;
    }
    public int getPlace() { return placeProperty().get(); }

    private StringProperty playersName;
    public StringProperty playersProperty() {
        if (playersName == null) playersName = new SimpleStringProperty(this, "playersName");
        return playersName;
    }
    public String getPlayersName() { return playersProperty().get(); }

    private ObjectProperty<LocalTime> runTime;
    public ObjectProperty<LocalTime> runTimeProperty() {
        if (runTime == null) runTime = new SimpleObjectProperty<>(this, "runTime");
        return runTime;
    }
    public LocalTime getRunTime() { return runTimeProperty().get(); }

    private StringProperty dateAgo;
    public StringProperty dateProperty() {
        if (dateAgo == null) dateAgo = new SimpleStringProperty(this, "dateAgo");
        return dateAgo;
    }
    public String getDateAgo() { return dateProperty().get(); }

}

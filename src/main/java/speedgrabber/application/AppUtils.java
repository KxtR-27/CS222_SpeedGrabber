package speedgrabber.application;

import org.ocpsoft.prettytime.PrettyTime;
import speedgrabber.records.Run;
import speedgrabber.records.interfaces.Player;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

// class and methods are intentionally package-private
class AppUtils {
    static String formatLeaderboard(List<Run> runs, List<Player[]> playersInRuns, int maxRuns) {
        StringBuilder leaderboardBuilder = new StringBuilder(String.format(
                "%-3s %-25s %-25s %s%n",
                "#", "Player", "Time", "Date"
        ));
        for (int i = 0; i < maxRuns && i < runs.size(); i++) {
            Run currentRun = runs.get(i);

            Player[] currentPlayers = playersInRuns.get(i);
            String playersDisplay = "";
            if (currentPlayers.length == 1)
                playersDisplay = currentPlayers[0].playername();
            if (currentPlayers.length >= 2)
                playersDisplay += "*";


            leaderboardBuilder.append(String.format(
                    "%-3s %-25s %-25s %s%n",
                    currentRun.place(),
                    playersDisplay,
                    currentRun.primaryTime(),
                    new PrettyTime().format(currentRun.dateOfRun())
            ));
        }

        return leaderboardBuilder.toString();
    }

    static void openLink(URI uri) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri);
        }
    }
}

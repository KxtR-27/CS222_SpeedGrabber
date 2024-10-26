package speedgrabber;

import org.apache.commons.io.IOUtils;
import speedgrabber.records.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiDataGrabber {
    private static String fetchJson(String url) throws IOException {
        URL gameUrl = URI.create(url).toURL();
        URLConnection connection = gameUrl.openConnection();
        connection.setRequestProperty("User-Agent", "SpeedGrabber/0.1a (connor.razo@bsu.edu)");
        InputStream urlStream = connection.getInputStream();

        return IOUtils.toString(urlStream, StandardCharsets.UTF_8);
    }

    public static Game getGame(String gameTitle) throws IOException {
        gameTitle = URLEncoder.encode(gameTitle, StandardCharsets.UTF_8);
        String gameLink = String.format("https://www.speedrun.com/api/v1/games/%s", gameTitle);

        return JsonReader.create(fetchJson(gameLink)).createGame();
    }

    public static List<Category> getCategories(Game game) throws IOException {
        return JsonReader.create(fetchJson(game.categoriesLink())).createCategoryList();
    }

    public static Leaderboard getLeaderboard(Category category, int maxRuns) throws IOException {
        return JsonReader.create(fetchJson(category.leaderboardLink())).createLeaderboard(maxRuns);
    }

    public static List<Run> getListOfRuns(Leaderboard leaderboard) throws IOException {
        List<Run> toReturn = new ArrayList<>();
        for (int i = 0; i < leaderboard.runLinks().size(); i++) {
            toReturn.add(getRun(
                    leaderboard.runLinks().get(i),
                    leaderboard.runPlaces().get(i)
            ));
        }

        return toReturn;
    }
    public static Run getRun(String runLink, int place) throws IOException {
        return JsonReader.create(fetchJson(runLink)).createRun(place);
    }
    public static Run getRun(String runLink) throws IOException {
        return getRun(runLink, -1);
    }
}

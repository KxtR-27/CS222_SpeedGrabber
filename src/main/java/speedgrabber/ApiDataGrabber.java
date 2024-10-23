package speedgrabber;

import org.apache.commons.io.IOUtils;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        return JsonReader.create(fetchJson(gameLink)).createGameData();
    }
    public static List<Category> getCategories(Game game) throws IOException {
        return JsonReader.create(fetchJson(game.linkToCategories())).createCategoryList();
    }
    public static Leaderboard getLeaderboard(Category category) throws IOException {
        return JsonReader.create(fetchJson(category.linkToLeaderboard())).createLeaderboard(20);
    }
}

package speedgrabber;

import org.apache.commons.io.IOUtils;
import speedgrabber.records.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiDataGrabber {
    private static final List<Identifiable> CACHED_IDENTIFIABLES = new ArrayList<>();

    private static boolean isCached(String identity) {
        for (Identifiable identifiable : CACHED_IDENTIFIABLES)
            if (identifiable.identify().equals(identity))
                return true;

        return false;
    }
    private static Identifiable getCachedIdentifiable(String identity) {
        for (Identifiable identifiable : CACHED_IDENTIFIABLES)
            if (identifiable.identify().equals(identity))
                return identifiable;

        return null;
    }

    private static String fetchJson(String url) throws IOException {
        URL dataUrl = URI.create(url).toURL();
        URLConnection connection = dataUrl.openConnection();
        connection.setRequestProperty("User-Agent", "SpeedGrabber/0.1a (connor.razo@bsu.edu)");
        InputStream urlStream = connection.getInputStream();

        String json = IOUtils.toString(urlStream, StandardCharsets.UTF_8);
        if (JsonReader.jsonContains404Error(json))
            throw new FileNotFoundException(String.format("%s returns status 404 (not found)", url));

        return json;
    }


    public static Game getGame(String gameTitle) throws IOException {
        gameTitle = URLEncoder.encode(gameTitle, StandardCharsets.UTF_8);
        String gameLink = String.format("https://www.speedrun.com/api/v1/games/%s", gameTitle);

        if (isCached(gameLink))
            return (Game) getCachedIdentifiable(gameLink);

        try {
            Game newGame = JsonReader.createGame(fetchJson(gameLink), fetchJson(gameLink + "/categories"));
            CACHED_IDENTIFIABLES.add(newGame);
            return newGame;
        } catch (FileNotFoundException error404) {
            throw new FileNotFoundException(String.format("%s returns status 404 (not found)", gameLink));
        }
    }

    public static List<Category> getListOfCategories(Game game) throws IOException {
        List<Category> toReturn = new ArrayList<>();
        for (String categoryLink : game.categoryLinks())
            toReturn.add(getCategory(categoryLink));

        return toReturn;
    }
    public static Category getCategory(String categoryLink) throws IOException {
        if (isCached(categoryLink))
            return (Category) getCachedIdentifiable(categoryLink);

        Category newCategory = JsonReader.createCategory(fetchJson(categoryLink));
        CACHED_IDENTIFIABLES.add(newCategory);
        return newCategory;
    }

    public static Leaderboard getLeaderboard(Category category, int maxRuns) throws IOException {
        if (isCached(category.leaderboardLink()))
            return (Leaderboard) getCachedIdentifiable(category.leaderboardLink());

        Leaderboard newLeaderboard = JsonReader.createLeaderboard(fetchJson(category.leaderboardLink()), maxRuns);
        CACHED_IDENTIFIABLES.add(newLeaderboard);
        return newLeaderboard;
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
        if (isCached(runLink))
            return (Run) getCachedIdentifiable(runLink);

        Run newRun = JsonReader.createRun(fetchJson(runLink), place);
        CACHED_IDENTIFIABLES.add(newRun);
        return newRun;
    }


    static void test_addToCache(Identifiable identifiable) {
        CACHED_IDENTIFIABLES.add(identifiable);
    }
    static boolean test_isCached(String identity) {
        return isCached(identity);
    }
    static Identifiable test_getCachedIdentifiable(String identity) {
        return getCachedIdentifiable(identity);
    }
}

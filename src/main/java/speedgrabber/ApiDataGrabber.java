package speedgrabber;

import org.apache.commons.io.IOUtils;
import speedgrabber.records.*;
import speedgrabber.records.interfaces.Identifiable;
import speedgrabber.records.interfaces.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiDataGrabber {
    private static final List<Identifiable> CACHED_IDENTIFIABLES = new ArrayList<>();
    private static final boolean ENABLE_CACHE_LOG = false;

    private static Identifiable getCachedIdentifiable(String identity) {
        for (Identifiable identifiable : CACHED_IDENTIFIABLES) {
            if (identifiable.identify().equals(identity)) {
                printCacheLog(String.format("[*] Fetched Identifiable (%s) [%s] from cache.%n", identifiable.getClass().getSimpleName(), identity));
                return identifiable;
            }
            else if (identifiable instanceof Game) {
                if (identifiable.identify().contains(identity)) {
                    printCacheLog(String.format("[*] Fetched Identifiable **Game** [%s] from cache using (%s).%n", identifiable.identify(), identity));
                    return identifiable;
                }
            }
        }

        System.out.printf("[!] Tried to fetch Identifiable with identity [%s], but found null.%n", identity);
        return null;
    }
    private static boolean isCached(String identity) {
        boolean isCached = false;

        for (Identifiable identifiable : CACHED_IDENTIFIABLES) {
            if (identifiable.identify().equals(identity)) {
                isCached = true;
            }
            else if (identifiable instanceof Game) {
                if (identifiable.identify().contains(identity))
                    isCached = true;
            }
        }

        printCacheLog(String.format("[?] Checked for cached identifiable with identity [%s] | [%b]%n", identity, isCached));
        return isCached;
    }
    private static void addToCache(Identifiable identifiable) {
        CACHED_IDENTIFIABLES.add(identifiable);
        printCacheLog(String.format("[+] Added to cache Identifiable (%s) with Identity [\"%s\"]%n", identifiable.getClass().getSimpleName(), identifiable.identify()));
    }
    private static void printCacheLog(String message) {
        if (ENABLE_CACHE_LOG) System.out.println(message);
    }

    private static String fetchJson(String url) throws IOException {
        URL dataUrl = URI.create(url).toURL();
        URLConnection connection = dataUrl.openConnection();
        connection.setRequestProperty("User-Agent", "SpeedGrabber/0.1a (connor.razo@bsu.edu)");
        InputStream urlStream = connection.getInputStream();

        String json = IOUtils.toString(urlStream, StandardCharsets.UTF_8);
        int errorCode = JsonReader.checkForKnownErrors(json);
        if (errorCode != -1)
            throw new FileNotFoundException(String.format("%s returns status %d", url, errorCode));

        return json;
    }


    public static Game getGame(String gameTitle) throws IOException {
        String gameLink = String.format("https://www.speedrun.com/api/v1/games/%s", gameTitle);

        if (isCached(gameTitle))
            return (Game) getCachedIdentifiable(gameTitle);

        try {
            Game newGame = JsonReader.createGame(
                    fetchJson(gameLink),
                    fetchJson(gameLink + "/categories"),
                    fetchJson(gameLink + "/levels")
            );
            addToCache(newGame);
            return newGame;
        } catch (FileNotFoundException error404) {
            throw new FileNotFoundException(String.format("%s returns status 404 (not found)", gameLink));
        }
    }

    public static List<Level> getListOfLevels(Game game) throws IOException {
        List<Level> toReturn = new ArrayList<>();
        for (String levelLink : game.levellinks())
            toReturn.add(getLevel(levelLink));

        return toReturn;
    }
    public static Level getLevel(String levellink) throws IOException {
        if (isCached(levellink))
            return (Level) getCachedIdentifiable(levellink);

        Level newLevel = JsonReader.createLevel(fetchJson(levellink));
        addToCache(newLevel);
        return newLevel;
    }

    public static List<Category> getListOfCategories(Game game) throws IOException {
        List<Category> toReturn = new ArrayList<>();
        for (String categoryLink : game.categorylinks())
            toReturn.add(getCategory(categoryLink));

        return toReturn;
    }
    public static Category getCategory(String categorylink) throws IOException {
        if (isCached(categorylink))
            return (Category) getCachedIdentifiable(categorylink);

        Category newCategory = JsonReader.createCategory(fetchJson(categorylink));
        addToCache(newCategory);
        return newCategory;
    }

    private static Leaderboard getLeaderboard(String leaderboardlink, int maxRuns) throws IOException {
        Leaderboard toReturn;

        if (isCached(leaderboardlink)) {
            toReturn = (Leaderboard) getCachedIdentifiable(leaderboardlink);
            if (toReturn != null && toReturn.runlinks().size() < maxRuns)
                JsonReader.populateLeaderboard(toReturn, maxRuns, fetchJson(leaderboardlink));
        }
        else {
            toReturn = JsonReader.createLeaderboard(fetchJson(leaderboardlink), maxRuns);
            addToCache(toReturn);
        }

        return toReturn;
    }
    public static Leaderboard getLeaderboard(Category category, int maxRuns) throws IOException {
        return getLeaderboard(category.leaderboardlink(), maxRuns);
    }
    public static Leaderboard getLeaderboard(Level level, Category category, int maxRuns) throws IOException {
        return getLeaderboard(String.format(
                "https://www.speedrun.com/api/v1/leaderboards/%s/level/%s/%s",
                category.gameID(), level.id(), category.id()
        ), maxRuns);
    }


    public static List<Run> getListOfRuns(Leaderboard leaderboard, int maxRuns) throws IOException {
        List<Run> toReturn = new ArrayList<>();

        int i;
        for (i = 0; i < leaderboard.numOfRunsInJson() && i < maxRuns; i++) {
            toReturn.add(getRun(
                    leaderboard.runlinks().get(i),
                    leaderboard.runplaces().get(i)
            ));
        }
        if (i < maxRuns - 1)
            JsonReader.populateLeaderboard(leaderboard, maxRuns, leaderboard.identify());

        return toReturn;
    }
    public static Run getRun(String runLink, int place) throws IOException {
        if (isCached(runLink))
            return (Run) getCachedIdentifiable(runLink);

        Run newRun = JsonReader.createRun(fetchJson(runLink), place);
        addToCache(newRun);
        return newRun;
    }

    public static List<Player[]> getPlayersInRuns(Leaderboard leaderboard, int maxRuns) throws IOException {
        List<Player[]> toReturn = new ArrayList<>();

        for (int i = 0; i < leaderboard.numOfRunsInJson() && i < maxRuns; i++) {
            int numOfPlayersInThisRun = leaderboard.playerlinks().get(i).length;
            Player[] playersInThisRun = new Player[numOfPlayersInThisRun];
            for (int j = 0; j < numOfPlayersInThisRun; j++) {
                String currentPlayerLink = leaderboard.playerlinks().get(i)[j];
                playersInThisRun[j] = getPlayer(currentPlayerLink);
            }
            toReturn.add(playersInThisRun);
        }

        return toReturn;
    }
    public static Player getPlayer(String playerlink) throws IOException {
        try {
            if (isCached(playerlink))
                return (Player) getCachedIdentifiable(playerlink);

            Player newPlayer = JsonReader.createPlayer(fetchJson(playerlink));
            addToCache(newPlayer);
            return newPlayer;

        } catch (FileNotFoundException playerNotFound) {
            return new Guest(null, "<not found>", null);
        }
    }


    static void test_addToCache(Identifiable identifiable) {
        addToCache(identifiable);
    }
    static boolean test_isCached(String identity) {
        return isCached(identity);
    }
    static Identifiable test_getCachedIdentifiable(String identity) {
        return getCachedIdentifiable(identity);
    }
}

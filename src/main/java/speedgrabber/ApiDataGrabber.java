package speedgrabber;

import org.apache.commons.io.IOUtils;
import speedgrabber.jsonreaders.*;
import speedgrabber.records.*;
import speedgrabber.records.interfaces.Identifiable;
import speedgrabber.records.interfaces.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// TODO: For sorting, get paginated runs [2]
// TODO: Uploading Splits with runs.
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
    private static void replaceInCache(Identifiable identifiable) {
        for (int i = 0; i < CACHED_IDENTIFIABLES.size(); i++) {
            if (identifiable.identify().equals(CACHED_IDENTIFIABLES.get(i).identify())) {
                CACHED_IDENTIFIABLES.set(i, identifiable);
                printCacheLog("[>] Identifiable with identity '" + identifiable.identify() + "' was replaced in cache.");
                return;
            }
        }

        printCacheLog("[?] Tried to replace identifiable with identity '" + identifiable.identify() + "' but none was found.");
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
            Game newGame = GameReader.create(
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

        Level newLevel = LevelReader.create(fetchJson(levellink));
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

        Category newCategory = CategoryReader.create(fetchJson(categorylink));
        addToCache(newCategory);
        return newCategory;
    }

    public static Leaderboard getLeaderboard(String leaderboardlink, int maxRuns) throws IOException {
        boolean leaderboardIsTotallyNew = true;

        if (isCached(leaderboardlink)) {
            Leaderboard leaderboard = (Leaderboard) getCachedIdentifiable(leaderboardlink);
            leaderboardIsTotallyNew = false;

            assert leaderboard != null;
            if (!LeaderboardReader.isMaxRunsOutOfBounds(leaderboard, maxRuns))
                return leaderboard;
        }

        Leaderboard newLeaderboard = LeaderboardReader.create(fetchJson(leaderboardlink), maxRuns);
        if ((leaderboardIsTotallyNew))
            addToCache(newLeaderboard);
        else
            replaceInCache(newLeaderboard);

        return newLeaderboard;
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

        return toReturn;
    }
    public static Run getRun(String runLink, int place) throws IOException {
        if (isCached(runLink))
            return (Run) getCachedIdentifiable(runLink);

        Run newRun = RunReader.create(fetchJson(runLink), place);
        addToCache(newRun);
        return newRun;
    }

    public static Player[] getPlayersInRun(Run run) throws IOException {
        int numOfPlayers = run.playerlinks().size();
        Player[] toReturn = new Player[numOfPlayers];

        for (int i = 0; i < numOfPlayers; i++)
            toReturn[i] = ApiDataGrabber.getPlayer(run.playerlinks().get(i));

        return toReturn;
    }
    public static Player getPlayer(String playerlink) throws IOException {
        try {
            if (isCached(playerlink))
                return (Player) getCachedIdentifiable(playerlink);

            Player newPlayer = PlayerReader.create(fetchJson(playerlink));
            addToCache(newPlayer);
            return newPlayer;

        } catch (FileNotFoundException playerNotFound) {
            return new Guest(null, "unfound guest", null);
        }
    }


    static Identifiable test_getCachedIdentifiable(String identity) {
        return getCachedIdentifiable(identity);
    }
    static boolean test_isCached(String identity) {
        return isCached(identity);
    }
    static void test_addToCache(Identifiable identifiable) {
        addToCache(identifiable);
    }
    static void test_replaceInCache(Identifiable identifiable) {
        replaceInCache(identifiable);
    }
}

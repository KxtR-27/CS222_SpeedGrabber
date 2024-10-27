package speedgrabber;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import speedgrabber.records.*;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    private static Object currentJsonDocument;

    private static void loadJsonDocument(String json) {
        currentJsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(json);
    }

    private static void cleanupEscapedList(List<String> escapedList) {
        for (String escapedString : escapedList) {
            while (escapedString.contains("\"")) {
                int quoteIndex = escapedString.indexOf("\"");
                escapedString = (escapedString.substring(0, quoteIndex)) + (escapedString.substring(quoteIndex + 2));
            }
            while (escapedString.contains("\\")) {
                int backslashIndex = escapedList.indexOf("\\");
                escapedString = (escapedString.substring(0, backslashIndex)) + (escapedString.substring(backslashIndex + 2));
            }
        }
    }

    // These methods are designed to be generic.
    // In the future, they will have more uses.
    // Then, the "SameParameterValue" warning will cease to exist.
    @SuppressWarnings("SameParameterValue")
    private static boolean pathExists(String key) {
        try {
            definiteScan(key);
            return true;
        }
        catch (PathNotFoundException e) {
            return false;
        }
    }
    private static List<String> indefiniteScan(String key) {
        List<String> indefiniteResults = JsonPath.read(currentJsonDocument, String.format("$..%s", key));
        cleanupEscapedList(indefiniteResults);
        return indefiniteResults;
    }

    private static String definiteScan(String keyPath) {
        return JsonPath.read(currentJsonDocument, String.format("$.%s", keyPath));
    }
    private static int scanInt(String key) {
        return JsonPath.read(currentJsonDocument, String.format("$.%s", key));
    }

    public static Game createGame(String gameJson, String categoriesJson, String levelsJson) {
        loadJsonDocument(categoriesJson);
        List<String> categoryLinks = indefiniteScan("links[0].uri");

        loadJsonDocument(levelsJson);
        List<String> levelLinks = indefiniteScan("links[0].uri");

        loadJsonDocument(gameJson);
        return new Game(
                definiteScan("data.weblink"),
                definiteScan("data.links[0].uri"),
                definiteScan("data.id"),
                definiteScan("data.abbreviation"),
                definiteScan("data.names.international"),

                categoryLinks,
                levelLinks
        );
    }
    public static Level createLevel(String levelJson) {
        loadJsonDocument(levelJson);
        return new Level(
                definiteScan("data.weblink"),
                definiteScan("data.links[0].uri"),
                definiteScan("data.id"),
                definiteScan("data.name"),

                definiteScan("data.links[1].uri")
        );
    }
    public static Category createCategory(String categoryJson) {
        loadJsonDocument(categoryJson);
        String type = definiteScan("data.type");

        switch (type) {
            case "per-game" -> {
                return new Category(
                        definiteScan("data.weblink"),
                        definiteScan("data.links[0].uri"),
                        definiteScan("data.id"),
                        definiteScan("data.name"),

                        definiteScan("data.links[5].uri"),
                        definiteScan("data.links[1].uri"),

                        type
                );
            }
            case "per-level" -> {
                return new Category(
                        definiteScan("data.weblink"),
                        definiteScan("data.links[0].uri"),
                        definiteScan("data.id"),
                        definiteScan("data.name"),

                        null,
                        definiteScan("data.links[1].uri"),

                        type
                );
            }
        }

        return null;
    }
    public static Leaderboard createLeaderboard(String leaderboardJson, int maxRuns) {
        loadJsonDocument(leaderboardJson);

        String webLink = definiteScan("data.weblink");

        String categoryLink = definiteScan("data.links[1].uri");
        String levelLink = (pathExists("data.links[2].uri")) ?
                (definiteScan("data.links[2].uri")) : null;
        String gameLink = definiteScan("data.links[0].uri");

        String timing = definiteScan("data.timing");
        int numOfRunsInJson = scanInt("data.runs.length()");
        ArrayList<String> runLinks = new ArrayList<>();
        ArrayList<Integer> runPlaces = new ArrayList<>();

        Leaderboard leaderboard = new Leaderboard(
                webLink, categoryLink, levelLink, gameLink, timing, numOfRunsInJson, runLinks, runPlaces
        );

        populateLeaderboard(leaderboard, maxRuns, leaderboardJson);

        return leaderboard;
    }
    public static Run createRun(String runJson, int place) {
        loadJsonDocument(runJson);
        return new Run(
                definiteScan("data.weblink"),
                definiteScan("data.links[0].uri"),
                definiteScan("data.id"),

                indefiniteScan("players[*].uri"),
                definiteScan("data.links[2].uri"),
                definiteScan("data.links[1].uri"),

                place,
                SGUtils.asLocalDate(definiteScan("data.date")),
                SGUtils.asLocalDateTime(definiteScan("data.submitted")),
                SGUtils.asLocalTime(definiteScan("data.times.primary"))
        );
    }

    public static void populateLeaderboard(Leaderboard leaderboard, int maxRuns, String leaderboardJson) {
        loadJsonDocument(leaderboardJson);

        for (int i = 0; i < maxRuns && i < leaderboard.numOfRunsInJson(); i++) {
            try {
                //noinspection unused - triggerOutOfBounds is vestigial
                String triggerOutOfBounds = leaderboard.runlinks().get(i);
            }
            catch (IndexOutOfBoundsException e) {
                leaderboard.runlinks().add(String.format(
                        "https://www.speedrun.com/api/v1/runs/%s",
                        definiteScan(String.format("data.runs[%d].run.id", i))
                ));
                leaderboard.runplaces().add(scanInt(String.format("data.runs[%d].place", i)));
            }
        }

    }

    public static boolean jsonContains404Error(String json) {
        loadJsonDocument(json);
        try {
            if (scanInt("status") == 404)
                return true;
        } catch (PathNotFoundException ignored) {}
        return false;
    }
}

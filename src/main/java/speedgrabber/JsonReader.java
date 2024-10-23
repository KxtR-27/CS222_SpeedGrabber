package speedgrabber;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    private final Object JSON;

    public static JsonReader create(String json) {
        return new JsonReader(json);
    }
    private JsonReader(String json) {
        JSON = Configuration.defaultConfiguration().jsonProvider().parse(json);
    }

    private String definiteScan(String keyPath) {
        return JsonPath.read(JSON, String.format("$.%s", keyPath));
    }
    private List<String> indefiniteScan(String key) {
        return JsonPath.read(JSON, String.format("$..%s", key));
    }
    private int scanLength(String key) {
        return JsonPath.read(JSON, String.format("$..%s.length()", key));
    }

    public Game createGameData() {
        return new Game(
                definiteScan("data.weblink"),
                definiteScan("data.links[0].uri"),
                definiteScan("data.id"),
                definiteScan("data.names.international"),

                definiteScan("data.links[3].uri")
        );
    }

    public List<Category> createCategoryList() {
        int listSize = scanLength("data");
        List<Category> toReturn = new ArrayList<>(listSize);

        for (int i = 0; i < listSize; i++) {
            if (definiteScan(String.format("data[%d].type", i)).equals("per-game"))
                toReturn.add(new Category(
                        definiteScan(String.format("data[%d].weblink", i)),
                        definiteScan(String.format("data[%d].links[0].uri", i)),
                        definiteScan(String.format("data[%d].id", i)),
                        definiteScan(String.format("data[%d].name", i)),

                        definiteScan(String.format("data[%d].links[5].uri", i))
                ));
        }

        return toReturn;
    }

    public Leaderboard createLeaderboard(int maxRuns) {
        String webLink = definiteScan("data.weblink");
        String gameID = definiteScan("data.game");
        String categoryID = definiteScan("data.category");

        String timing = definiteScan("data.timing");

        List<String> runIDs = new ArrayList<>(maxRuns);
        for (int i = 0; i < maxRuns && i < scanLength("data.runs"); i++) {
            runIDs.add(definiteScan(String.format("data.runs[%d].run.id", i)));
        }

        return new Leaderboard(webLink, gameID, categoryID, timing, runIDs);
    }
}

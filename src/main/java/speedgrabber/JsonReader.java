package speedgrabber;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import speedgrabber.records.Category;
import speedgrabber.records.Game;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private int scanInt(String key) {
        return JsonPath.read(JSON, String.format("$.%s", key));
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

                        definiteScan(String.format("data[%d].links[5].uri", i)),
                        definiteScan(String.format("data[%d].links[1].uri", i))
                ));
        }

        return toReturn;
    }

    public Leaderboard createLeaderboard(int maxRuns) throws IOException {
        String webLink = definiteScan("data.weblink");

        String gameLink = definiteScan("data.links[0].uri");
        String categoryLink = definiteScan("data.links[1].uri");

        String timing = definiteScan("data.timing");
        LinkedHashMap<Integer, Run> runs = new LinkedHashMap<>();

        for (int i = 0; i < maxRuns && i < scanLength("data.runs"); i++) {
            runs.put(
                    scanInt(String.format("data.runs[%d].place", i)),
                    ApiDataGrabber.getRun(definiteScan(String.format("data.runs[%d].run.id", i)))
            );
        }

        return new Leaderboard(webLink, gameLink, categoryLink, timing, runs);
    }

    public Run createRun() {
        return new Run(
                definiteScan("data.weblink"),
                definiteScan("data.links[0].uri"),
                definiteScan("data.id"),

                definiteScan("data.links[1].uri"),
                definiteScan("data.links[2].uri"),
                indefiniteScan("players[*].uri"),

                definiteScan("data.submitted"),
                definiteScan("data.times.primary")
        );
    }
}

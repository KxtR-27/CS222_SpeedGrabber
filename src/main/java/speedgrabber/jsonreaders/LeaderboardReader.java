package speedgrabber.jsonreaders;

import speedgrabber.records.Leaderboard;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardReader extends JsonReader {

    private static Leaderboard leaderboard;

    private static int maxRuns;
    private static int numOfRunsInJson;

    private static final HashMap<String, String> parameters = new HashMap<>();
    private static final ArrayList<String> runlinks = new ArrayList<>();
    private static final ArrayList<Integer> runplaces = new ArrayList<>();
    private static final ArrayList<String[]> runPlayerlinks = new ArrayList<>();

    public static Leaderboard create(String leaderboardJson, int maxRuns) {
        loadJsonDocument(leaderboardJson);
        LeaderboardReader.maxRuns = maxRuns;
        setup();

        return leaderboard;
    }

    private static void setup() {
        numOfRunsInJson = (int) definiteScan("data.runs.length()");

        setParameters();
        setRunLists();
        setLeaderboard();
    }
    private static void setParameters() {
        parameters.clear();

        parameters.put("weblink", (String) definiteScan("data.weblink"));
        parameters.put("gamelink", (String) definiteScan("data.links[0].uri"));
        parameters.put("categorylink", (String) definiteScan("data.links[1].uri"));

        String levellink = (pathExists("data.links[2].uri"))
                ? (String) definiteScan("data.links[2].uri")
                : null;
        parameters.put("levellink", levellink);

        parameters.put("timing", (String) definiteScan("data.timing"));
    }
    private static void setRunLists() {
        runlinks.clear(); runplaces.clear(); runPlayerlinks.clear();

        for (int i = 0; i < maxRuns && i < numOfRunsInJson; i++) {
            runlinks.add(String.format(
                    "%s%s", "https://www.speedrun.com/api/v1/runs/",
                    definiteScan(String.format("data.runs[%d].run.id", i))
            ));
            runplaces.add((int) definiteScan(String.format(
                    "data.runs[%d].place", i
            )));
            runPlayerlinks.add(setPlayerlinksArray(i));
        }
    }
    private static String[] setPlayerlinksArray(int indexInRuns) {
        String[] toReturn = new String[(int) definiteScan(String.format("data.runs[%d].run.players.length()", indexInRuns))];

        for (int i = 0; i < toReturn.length; i++)
            toReturn[i] = (String) definiteScan(String.format("data.runs[%d].run.players[%d].uri", indexInRuns, i));

        return toReturn;
    }
    private static void setLeaderboard() {
        leaderboard = new Leaderboard(
                parameters.get("weblink"),

                parameters.get("categorylink"),
                parameters.get("levellink"),
                parameters.get("gamelink"),

                parameters.get("timing"),
                numOfRunsInJson,
                runlinks, runplaces, runPlayerlinks
        );
    }

    public static boolean isMaxRunsOutOfBounds(Leaderboard leaderboard, int maxRuns) {
        return (leaderboard.runlinks().size() < maxRuns);
    }
}

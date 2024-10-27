package speedgrabber.records;

import java.util.ArrayList;

public record Leaderboard(
        String weblink,

        String categorylink,
        String levellink,
        String gamelink,

        String timing,
        int numOfRunsInJson,
        ArrayList<String> runlinks,
        ArrayList<Integer> runplaces

) implements Identifiable {
    @Override
    public String identify() {
        String categoryID = categorylink.split("/")[categorylink.split("/").length - 1];
        String gameID = gamelink.split("/")[gamelink.split("/").length - 1];

        if (levellink != null) {
            String levelID = levellink.split("/")[levellink.split("/").length - 1];
            return "https://www.speedrun.com/api/v1/leaderboards/" + gameID + "/level/" + levelID + "/" + categoryID;
        }

        return "https://www.speedrun.com/api/v1/leaderboards/" + gameID + "/category/" + categoryID;
    }
}

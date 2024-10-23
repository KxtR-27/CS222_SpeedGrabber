package speedgrabber.records;

import java.util.List;

public record Leaderboard(
        String weblink,
        String gameID,
        String categoryID,

        String timing,

        List<String> runIDs
) {
    public String getLinkToRun(int index) {
        return String.format("https://www.speedrun.com/api/v1/runs/%s", runIDs.get(index));
    }
}

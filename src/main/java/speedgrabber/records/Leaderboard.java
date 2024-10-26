package speedgrabber.records;

import java.util.List;

public record Leaderboard(
        String weblink,

        String categoryLink,
        String gameLink,

        String timing,
        List<String> runLinks,
        List<Integer> runPlaces

) implements Identifiable {
    @Override
    public String identify() {
        String gameID = gameLink.split("/")[gameLink.split("/").length - 1];
        String categoryID = categoryLink.split("/")[categoryLink.split("/").length - 1];

        return "https://www.speedrun.com/api/v1/leaderboards/" + gameID + "/category/" + categoryID;
    }
}

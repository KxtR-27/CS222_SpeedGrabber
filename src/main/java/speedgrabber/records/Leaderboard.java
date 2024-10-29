package speedgrabber.records;

import speedgrabber.SGUtils;
import speedgrabber.records.interfaces.Identifiable;

import java.util.ArrayList;
import java.util.Objects;

public record Leaderboard(
        String weblink,

        String categorylink,
        String levellink,
        String gamelink,

        String timing,
        int numOfRunsInJson,
        ArrayList<String> runlinks,
        ArrayList<Integer> runplaces,
        ArrayList<String[]> playerlinks

) implements Identifiable {
    @Override
    public String identify() {
        String gameID = SGUtils.grabEndOfSplit(gamelink, "/");
        String categoryID = SGUtils.grabEndOfSplit(categorylink, "/");
        String levelID = (levellink != null) ? SGUtils.grabEndOfSplit(levellink, "/") : null;

        return (levellink == null) ?
                String.format(
                        "https://www.speedrun.com/api/v1/leaderboards/%s/category/%s",
                        gameID, categoryID
                ):
                String.format(
                        "https://www.speedrun.com/api/v1/leaderboards/%s/level/%s/%s",
                        gameID, levelID, categoryID
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return true;

        Leaderboard that = (Leaderboard) o;

        boolean playerlinksAreEqual = true;
        for (int i = 0; i < this.playerlinks.size(); i++) {
            for (int j = 0; j < this.playerlinks.get(i).length; j++) {
                if (!this.playerlinks.get(i)[j].equals(that.playerlinks.get(i)[j])) {
                    playerlinksAreEqual = false;
                    break;
                }
            }
        }

        return numOfRunsInJson == that.numOfRunsInJson
                && Objects.equals(timing, that.timing)
                && Objects.equals(weblink, that.weblink)
                && Objects.equals(gamelink, that.gamelink)
                && Objects.equals(levellink, that.levellink)
                && Objects.equals(categorylink, that.categorylink)
                && Objects.equals(runlinks, that.runlinks)
                && Objects.equals(runplaces, that.runplaces)
                && playerlinksAreEqual;
    }
}

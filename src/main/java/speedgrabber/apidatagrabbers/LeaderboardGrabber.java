package speedgrabber.apidatagrabbers;

import speedgrabber.jsonreaders.LeaderboardReader;
import speedgrabber.records.Leaderboard;

import java.io.IOException;

public class LeaderboardGrabber extends ApiDataGrabber {
    public static Leaderboard grab(String leaderboardlink, int maxRuns) throws IOException {
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
}

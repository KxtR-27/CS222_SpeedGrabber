package speedgrabber.apidatagrabbers;

import speedgrabber.jsonreaders.RunReader;
import speedgrabber.records.Leaderboard;
import speedgrabber.records.Run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunGrabber extends ApiDataGrabber {
    public static List<Run> grabList(Leaderboard leaderboard, int maxRuns) throws IOException {
        List<Run> toReturn = new ArrayList<>();

        int i;
        for (i = 0; i < leaderboard.numOfRunsInJson() && i < maxRuns; i++) {
            toReturn.add(grab(
                    leaderboard.runlinks().get(i),
                    leaderboard.runplaces().get(i)
            ));
        }

        return toReturn;
    }

    public static Run grab(String runLink, int place) throws IOException {
        if (isCached(runLink))
            return (Run) getCachedIdentifiable(runLink);

        Run newRun = RunReader.create(fetchJson(runLink), place);
        addToCache(newRun);
        return newRun;
    }
}

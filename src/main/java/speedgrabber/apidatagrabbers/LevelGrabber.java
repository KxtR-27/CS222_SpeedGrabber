package speedgrabber.apidatagrabbers;

import speedgrabber.jsonreaders.LevelReader;
import speedgrabber.records.Game;
import speedgrabber.records.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelGrabber extends ApiDataGrabber {
    public static List<Level> grabList(Game game) throws IOException {
        List<Level> toReturn = new ArrayList<>();
        for (String levelLink : game.levellinks())
            toReturn.add(grab(levelLink));

        return toReturn;
    }

    public static Level grab(String levellink) throws IOException {
        if (isCached(levellink))
            return (Level) getCachedIdentifiable(levellink);

        Level newLevel = LevelReader.create(fetchJson(levellink));
        addToCache(newLevel);
        return newLevel;
    }
}

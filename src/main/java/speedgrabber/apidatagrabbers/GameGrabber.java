package speedgrabber.apidatagrabbers;

import speedgrabber.jsonreaders.GameReader;
import speedgrabber.records.Game;

import java.io.IOException;

public class GameGrabber extends ApiDataGrabber {
    public static Game grab(String gameTitle) throws IOException {
        String gameLink = String.format("https://www.speedrun.com/api/v1/games/%s", gameTitle);

        if (isCached(gameTitle))
            return (Game) getCachedIdentifiable(gameTitle);

        Game newGame = GameReader.create(
                fetchJson(gameLink),
                fetchJson(gameLink + "/categories"),
                fetchJson(gameLink + "/levels")
        );
        addToCache(newGame);
        return newGame;
    }
}

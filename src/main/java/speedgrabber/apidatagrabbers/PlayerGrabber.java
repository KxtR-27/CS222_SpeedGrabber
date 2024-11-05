package speedgrabber.apidatagrabbers;

import speedgrabber.jsonreaders.PlayerReader;
import speedgrabber.records.Guest;
import speedgrabber.records.Run;
import speedgrabber.records.interfaces.Player;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayerGrabber extends ApiDataGrabber {
    public static Player[] grabArrayFromRun(Run run) throws IOException {
        int numOfPlayers = run.playerlinks().size();
        Player[] toReturn = new Player[numOfPlayers];

        for (int i = 0; i < numOfPlayers; i++)
            toReturn[i] = grab(run.playerlinks().get(i));

        return toReturn;
    }
    public static Player grab(String playerlink) throws IOException {
        try {
            if (isCached(playerlink))
                return (Player) getCachedIdentifiable(playerlink);

            Player newPlayer = PlayerReader.create(fetchJson(playerlink));
            addToCache(newPlayer);
            return newPlayer;

        } catch (FileNotFoundException playerNotFound) {
            return new Guest(null, "unfound guest", null);
        }
    }
}

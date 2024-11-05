package speedgrabber.jsonreaders;

import speedgrabber.records.Guest;
import speedgrabber.records.User;
import speedgrabber.records.interfaces.Player;

public class PlayerReader extends JsonReader {

    public static Player create(String playerJson) {
        loadJsonDocument(playerJson);

        return (pathExists("data.id"))
                ? createUser()
                : createGuest();
    }

    private static Player createUser() {
        return new User(
                (String) definiteScan("data.weblink"),
                (String) definiteScan("data.links[0].uri"),
                (String) definiteScan("data.id"),
                (String) definiteScan("data.names.international"),

                (String) definiteScan("data.role"),
                (String) definiteScan("data.links[1].uri"),
                (String) definiteScan("data.links[3].uri"),

                (pathExists("data.twitch.uri")) ? (String) definiteScan("data.twitch.uri") : null,
                (pathExists("data.hitbox.uri")) ? (String) definiteScan("data.hitbox.uri") : null,
                (pathExists("data.youtube.uri")) ? (String) definiteScan("data.youtube.uri") : null,
                (pathExists("data.twitter.uri")) ? (String) definiteScan("data.twitter.uri") : null,
                (pathExists("data.speedrunslive.uri")) ? (String) definiteScan("data.speedrunslive.uri") : null
        );
    }
    private static Player createGuest() {
        return new Guest(
                (String) definiteScan("data.links[0].uri"),
                (String) definiteScan("data.name"),

                (String) definiteScan("data.links[1].uri")
        );
    }

}

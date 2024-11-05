package speedgrabber.jsonreaders;

import speedgrabber.records.Game;

import java.util.List;

public class GameReader extends JsonReader {
    private static Game game;
    private static List<String> categoryLinks;
    private static List<String> levelLinks;

    public static Game create(String gameJson, String categoriesJson, String levelsJson) {
        setCategoryLinks(categoriesJson);
        setLevelLinks(levelsJson);
        setGame(gameJson);

        return game;
    }

    private static void setGame(String gameJson) {
        loadJsonDocument(gameJson);
        game = new Game(
                (String) definiteScan("data.weblink"),
                (String) definiteScan("data.links[0].uri"),
                (String) definiteScan("data.id"),
                (String) definiteScan("data.abbreviation"),
                (String) definiteScan("data.names.international"),

                categoryLinks,
                levelLinks
        );
    }
    private static void setCategoryLinks(String categoriesJson) {
        loadJsonDocument(categoriesJson);
        categoryLinks = indefiniteScan("links[0].uri");
    }
    private static void setLevelLinks(String levelsJson) {
        loadJsonDocument(levelsJson);
        levelLinks = indefiniteScan("links[0].uri");
    }

}

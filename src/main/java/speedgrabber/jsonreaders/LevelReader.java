package speedgrabber.jsonreaders;

import speedgrabber.records.Level;

public class LevelReader extends JsonReader {

    public static Level create(String levelJson) {
        loadJsonDocument(levelJson);

        return new Level(
                (String) definiteScan("data.weblink"),
                (String) definiteScan("data.links[0].uri"),
                (String) definiteScan("data.id"),
                (String) definiteScan("data.name"),

                (String) definiteScan("data.links[1].uri")
        );
    }

}

package speedgrabber.jsonreaders;

import speedgrabber.SGUtils;
import speedgrabber.records.Run;

public class RunReader extends JsonReader {

    public static Run create(String runJson, int place) {
        loadJsonDocument(runJson);

        return new Run(
                (String) definiteScan("data.weblink"),
                (String) definiteScan("data.links[0].uri"),
                (String) definiteScan("data.id"),

                indefiniteScan("players[*].uri"),
                (String) definiteScan("data.links[2].uri"),
                (String) definiteScan("data.links[1].uri"),

                place,
                SGUtils.asLocalDate((String) definiteScan("data.date")),
                SGUtils.asLocalDateTime((String) definiteScan("data.submitted")),
                SGUtils.asLocalTime((String) definiteScan("data.times.primary"))
        );
    }

}

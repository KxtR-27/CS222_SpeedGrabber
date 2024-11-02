package speedgrabber.jsonreaders;

import speedgrabber.records.Category;

public class CategoryReader extends JsonReader {

    public static Category create(String categoryJson) {
        loadJsonDocument(categoryJson);

        return new Category(
                (String) definiteScan("data.weblink"),
                (String) definiteScan("data.links[0].uri"),
                (String) definiteScan("data.id"),
                (String) definiteScan("data.name"),

                (pathExists("data.links[5]")) ? (String) definiteScan("data.links[5].uri") : null,
                (String) definiteScan("data.links[1].uri"),

                (String) definiteScan("data.type")
        );
    }

}

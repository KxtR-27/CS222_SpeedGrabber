package speedgrabber.records;

import java.util.List;

public record Game(
        String webLink,
        String selfLink,
        String id,
        String abbreviation,
        String name,

        List<String> categoryLinks,
        List<String> levelLinks

) implements Identifiable {
    @Override
    public String identify() { return String.format("%s+%s", id, abbreviation); }
}
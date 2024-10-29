package speedgrabber.records;

import speedgrabber.records.interfaces.Identifiable;

import java.util.List;

public record Game(
        String weblink,
        String selflink,
        String id,
        String abbreviation,
        String name,

        List<String> categorylinks,
        List<String> levellinks

) implements Identifiable {
    @Override
    public String identify() { return String.format("%s+%s", id, abbreviation); }
}
package speedgrabber.records;

public record Level(
        String webLink,
        String selfLink,
        String id,
        String name,

        String gameLink

) implements Identifiable {
    @Override
    public String identify() {
        return selfLink;
    }

    @Override
    public String toString() {
        return name;
    }
}

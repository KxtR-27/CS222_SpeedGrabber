package speedgrabber.records;

public record Category(
        String weblink,
        String selfLink,
        String id,
        String name,

        String leaderboardLink,
        String gameLink

) implements Identifiable {
    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getSelfLink() {
        return selfLink;
    }
}
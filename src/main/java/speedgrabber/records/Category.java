package speedgrabber.records;

public record Category(
        String weblink,
        String selfLink,
        String id,
        String name,

        String linkToLeaderboard
) {
    @Override
    public String toString() {
        return name;
    }
}
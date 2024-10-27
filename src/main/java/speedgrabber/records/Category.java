package speedgrabber.records;

public record Category(
        String weblink,
        String selflink,
        String id,
        String name,

        String leaderboardlink,
        String gamelink,

        String type

) implements Identifiable {
    @Override
    public String toString() {
        return (this.type.equals("per-level") ? (name + "*") : name);
    }

    @Override
    public String identify() {
        return selflink;
    }

    public String gameID() {
        String[] toReturn = gamelink.split("/");
        return toReturn[toReturn.length - 1];
    }
}
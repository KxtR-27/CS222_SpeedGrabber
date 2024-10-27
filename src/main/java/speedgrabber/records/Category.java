package speedgrabber.records;

public record Category(
        String webLink,
        String selfLink,
        String id,
        String name,

        String leaderboardLink,
        String gameLink,

        String type

) implements Identifiable {
    @Override
    public String toString() {
        return (this.type.equals("per-level") ? (name + "*") : name);
    }

    @Override
    public String identify() {
        return selfLink;
    }

    public String gameID() {
        String[] toReturn = gameLink.split("/");
        return toReturn[toReturn.length - 1];
    }
}
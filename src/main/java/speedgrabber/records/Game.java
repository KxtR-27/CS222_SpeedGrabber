package speedgrabber.records;

public record Game(
        String weblink,
        String selfLink,
        String id,
        String name,

        String categoryLink

) implements Identifiable {
    @Override
    public String getSelfLink() {return selfLink;}
}
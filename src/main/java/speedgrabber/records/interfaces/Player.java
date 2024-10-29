package speedgrabber.records.interfaces;

public interface Player extends Identifiable {
    String playername();
    String playerlink();

    @Override
    default String identify() {
        return playerlink();
    }
}

package speedgrabber.records;

import java.util.List;

public record Game(
        String weblink,
        String selfLink,
        String id,
        String name,

        List<String> categoryLinks

) implements Identifiable {
    @Override
    public String identify() {return selfLink;}
}
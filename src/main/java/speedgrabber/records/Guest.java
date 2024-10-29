package speedgrabber.records;

import speedgrabber.records.interfaces.Player;

public record Guest(
        String selflink,
        String name,

        String runsLink

) implements Player {
    @Override
    public String playername() {
        return name + "*";
    }
    @Override
    public String playerlink() {
        return selflink;
    }

    @Override
    public String identify() {
        return selflink;
    }
}

package speedgrabber.records;

import speedgrabber.records.interfaces.Player;

public record User(
  String weblink,
  String selflink,
  String id,
  String name,

  String role,
  String runslink,
  String pbslink,

  String twitchLink,
  String hitboxlink,
  String youtubelink,
  String twitterlink,
  String speedrunslivelink

) implements Player {
    public User(String weblink, String selflink, String id, String name, String role, String runslink, String pbslink) {
        this(weblink, selflink, id, name, role, runslink, pbslink, null, null, null, null, null);
    }
    @Override
    public String playername() {
        return name;
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

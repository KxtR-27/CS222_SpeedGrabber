package speedgrabber.records;

import java.util.LinkedHashMap;

public record Leaderboard(
        String weblink,

        String gameLink,
        String categoryLink,

        String timing,
        LinkedHashMap<Integer, Run> runs
) {}

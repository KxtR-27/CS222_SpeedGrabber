package speedgrabber.records;

import java.util.List;

public record Leaderboard(
        String weblink,

        String gameLink,
        String categoryLink,

        String timing,
        List<Run> runs
) {}

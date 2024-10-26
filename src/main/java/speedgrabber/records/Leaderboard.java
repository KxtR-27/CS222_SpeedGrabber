package speedgrabber.records;

import java.util.List;

public record Leaderboard(
        String weblink,

        String linkToCategory,
        String linkToGame,

        String timing,
        List<Run> runs
) {}

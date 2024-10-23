package speedgrabber.records;

import java.util.List;

public record Run(
        String webLink,
        String selfLink,
        String id,

        int place,
        String date_submitted,
        int realtime_length,

        List<String> playerLinks
) {}

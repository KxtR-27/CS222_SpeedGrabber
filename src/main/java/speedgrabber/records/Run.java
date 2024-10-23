package speedgrabber.records;

import java.util.List;

public record Run(
        String webLink,
        String selfLink,
        String id,

        String gameLink,
        String categoryLink,
        List<String> playerLinks,

        String date_submitted,
        String primaryTime
) {}

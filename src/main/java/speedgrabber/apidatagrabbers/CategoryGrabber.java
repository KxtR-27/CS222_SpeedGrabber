package speedgrabber.apidatagrabbers;

import speedgrabber.jsonreaders.CategoryReader;
import speedgrabber.records.Category;
import speedgrabber.records.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryGrabber extends ApiDataGrabber {
    public static List<Category> grabList(Game game) throws IOException {
        List<Category> toReturn = new ArrayList<>();
        for (String categoryLink : game.categorylinks())
            toReturn.add(grab(categoryLink));

        return toReturn;
    }

    public static Category grab(String categorylink) throws IOException {
        if (isCached(categorylink))
            return (Category) getCachedIdentifiable(categorylink);

        Category newCategory = CategoryReader.create(fetchJson(categorylink));
        addToCache(newCategory);
        return newCategory;
    }
}

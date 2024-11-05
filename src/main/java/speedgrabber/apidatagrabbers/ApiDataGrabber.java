package speedgrabber.apidatagrabbers;

import org.apache.commons.io.IOUtils;
import speedgrabber.jsonreaders.JsonReader;
import speedgrabber.records.Game;
import speedgrabber.records.interfaces.Identifiable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// TODO: For sorting, get paginated runs [2]
public abstract class ApiDataGrabber {
    private static final List<Identifiable> CACHED_IDENTIFIABLES = new ArrayList<>();
    private static final boolean ENABLE_CACHE_LOG = false;

    protected static Identifiable getCachedIdentifiable(String identity) {
        for (Identifiable identifiable : CACHED_IDENTIFIABLES) {
            if (identifiable.identify().equals(identity)) {
                printCacheLog(String.format("[*] Fetched Identifiable (%s) [%s] from cache.%n", identifiable.getClass().getSimpleName(), identity));
                return identifiable;
            }
            else if (identifiable instanceof Game) {
                if (identifiable.identify().contains(identity)) {
                    printCacheLog(String.format("[*] Fetched Identifiable **Game** [%s] from cache using (%s).%n", identifiable.identify(), identity));
                    return identifiable;
                }
            }
        }

        System.out.printf("[!] Tried to fetch Identifiable with identity [%s], but found null.%n", identity);
        return null;
    }
    protected static boolean isCached(String identity) {
        boolean isCached = false;

        for (Identifiable identifiable : CACHED_IDENTIFIABLES) {
            if (identifiable.identify().equals(identity)) {
                isCached = true;
            }
            else if (identifiable instanceof Game) {
                if (identifiable.identify().contains(identity))
                    isCached = true;
            }
        }

        printCacheLog(String.format("[?] Checked for cached identifiable with identity [%s] | [%b]%n", identity, isCached));
        return isCached;
    }
    protected static void addToCache(Identifiable identifiable) {
        CACHED_IDENTIFIABLES.add(identifiable);
        printCacheLog(String.format("[+] Added to cache Identifiable (%s) with Identity [\"%s\"]%n", identifiable.getClass().getSimpleName(), identifiable.identify()));
    }
    protected static void replaceInCache(Identifiable identifiable) {
        for (int i = 0; i < CACHED_IDENTIFIABLES.size(); i++) {
            if (identifiable.identify().equals(CACHED_IDENTIFIABLES.get(i).identify())) {
                CACHED_IDENTIFIABLES.set(i, identifiable);
                printCacheLog("[>] Identifiable with identity '" + identifiable.identify() + "' was replaced in cache.");
                return;
            }
        }

        printCacheLog("[?] Tried to replace identifiable with identity '" + identifiable.identify() + "' but none was found.");
    }
    protected static void printCacheLog(String message) {
        if (ENABLE_CACHE_LOG) System.out.println(message);
    }

    protected static String fetchJson(String url) throws IOException {
        URL dataUrl = URI.create(url).toURL();
        URLConnection connection = dataUrl.openConnection();
        connection.setRequestProperty("User-Agent", "SpeedGrabber/0.1a (connor.razo@bsu.edu)");
        InputStream urlStream = connection.getInputStream();

        String json = IOUtils.toString(urlStream, StandardCharsets.UTF_8);
        int errorCode = JsonReader.checkForKnownErrors(json);
        if (errorCode != -1)
            throw new FileNotFoundException(String.format("%s returns status %d", url, errorCode));

        return json;
    }
}

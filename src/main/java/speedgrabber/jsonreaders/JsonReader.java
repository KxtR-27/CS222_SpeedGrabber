package speedgrabber.jsonreaders;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.util.List;

public abstract class JsonReader {
    protected static Object currentJsonDocument;
    protected static void loadJsonDocument(String json) {
        currentJsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(json);
    }

    protected static Object definiteScan(String keyPath) {
        return JsonPath.read(currentJsonDocument, String.format("$.%s", keyPath));
    }
    protected static List<String> indefiniteScan(String key) {
        List<String> indefiniteResults = JsonPath.read(currentJsonDocument, String.format("$..%s", key));
        cleanupEscapedList(indefiniteResults);
        return indefiniteResults;
    }
    private static void cleanupEscapedList(List<String> escapedList) {
        for (String escapedString : escapedList) {
            while (escapedString.contains("\"")) {
                int quoteIndex = escapedString.indexOf("\"");
                escapedString = (escapedString.substring(0, quoteIndex)) + (escapedString.substring(quoteIndex + 2));
            }
            while (escapedString.contains("\\")) {
                int backslashIndex = escapedList.indexOf("\\");
                escapedString = (escapedString.substring(0, backslashIndex)) + (escapedString.substring(backslashIndex + 2));
            }
        }
    }

    protected static boolean pathExists(String key) {
        try {
            definiteScan(key);
            return true;
        }
        catch (PathNotFoundException e) {
            return false;
        }
    }
    public static int checkForKnownErrors(String json) {
        if (jsonContainsError(json, 404))
            return 404;
        if (jsonContainsError(json, 400))
            return 400;

        return -1;
    }
    private static boolean jsonContainsError(String json, int status) {
        loadJsonDocument(json);
        if (pathExists("status") && (int) definiteScan("status") == status) {
            System.err.println("Json contains error code" + status);
            return true;
        }
        return false;
    }
}
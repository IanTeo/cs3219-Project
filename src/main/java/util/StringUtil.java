package util;

public class StringUtil {
    public static String parseString(String toParse) {
        return toParse.trim()
                .replace("\n", " ")
                .replaceAll("\\s+", " ");
    }
}

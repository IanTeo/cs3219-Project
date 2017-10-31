package util;

import java.util.Arrays;
import java.util.Collection;

public class StringUtil {
    /**
     * Takes a String, turn it to lower case and replaces all '\n' and spaces with a single space
     */
    public static String parseString(String toParse) {
        return toParse.trim()
                .toLowerCase()
                .replace("\n", " ")
                .replaceAll("\\s+", " ");
    }

    public static String sanitise(String sanitise) {
        return sanitise.trim()
                .replace("\n", " ")
                .replaceAll("\\s+", " ");
    }

    public static boolean containsIgnoreCaseVenue(String str, String searchStr) {
        if(str == null || searchStr == null) return false;
        Collection<String> splitStrs = Arrays.asList(str.split("[\\s\\p{Punct}]+"));

        return searchStr.length() == 0 ||
                splitStrs.stream().anyMatch(splitStr -> splitStr.equalsIgnoreCase(searchStr));
    }

    public static boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) {
                return true;
            }
        }

        return false;
    }
}

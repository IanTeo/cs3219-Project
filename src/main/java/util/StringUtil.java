package util;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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

    // Check if exact match of searchStr is contained in str, ignoring case and punctuation
    public static boolean containsMatchIgnoreCaseAndPunctuation(String str, String searchStr) {
        if(str == null || searchStr == null) return false;
        if (str.equalsIgnoreCase(searchStr)) return true;

        for (String split : str.split("[\\s\\p{Punct}]+")) {
            if (split.equalsIgnoreCase(searchStr)) {
                return true;
            }
        }
        return false;
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

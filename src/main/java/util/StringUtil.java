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

    public static boolean containsIgnoreCaseVenue(String str1, String str2) {
        if(str1 == null || str2 == null) return false;
        Collection<String> splitStrs1 = Arrays.stream(str1.split("[\\s\\p{Punct}]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        Collection<String> splitStrs2 = Arrays.stream(str2.split("[\\s\\p{Punct}]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return str1.length() == 0 || str2.length() == 0 ||
                splitStrs1.containsAll(splitStrs2) || splitStrs2.containsAll(splitStrs1);
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

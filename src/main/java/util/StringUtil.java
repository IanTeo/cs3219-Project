package util;

public class StringUtil {
    public static String parseString(String toParse) {
        return toParse.trim()
                .replace("\n", " ")
                .replaceAll("\\s+", " ");
    }
    
    public static String formatYearCountReply(int[] yearCounts, int startYear) {
        StringBuilder builder = new StringBuilder();
        builder.append(startYear).append(": ").append(yearCounts[0]);
        for (int i = 1; i < yearCounts.length; i++) {
            builder.append(String.format(", %s: %s", startYear + i, yearCounts[i]));
        }
        return builder.toString();
    }
}

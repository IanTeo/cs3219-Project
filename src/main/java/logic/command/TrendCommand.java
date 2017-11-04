package logic.command;

import model.Author;
import model.Model;
import model.Paper;
import util.JsonUtil;
import util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class TrendCommand implements Command{
    public static final String COMMAND_WORD = "trend";
    public static final String HELP = "Error: %s\nUsage: trend [startYear]-[endYear] [type]\n" +
            "This command returns a JSON file representing the venue/author trend for the specified year range";
    private Model model;
    private int startYear;
    private int endYear;
    private String seriesType;

    public String execute() {
        // Map of (Series -> Map of (Year -> Paper Count))
        Map<String, Map<Integer, Integer>> map;

        switch (seriesType) {
            case "venue" :
                map = getVenueMap();
                break;

            case "author" :
                map = getAuthorMap();
                break;

            default :
                return String.format(HELP, "Invalid type");
        }

        // Filters should happen here (If need to filter)
        filterTrivialData(map);

        return JsonUtil.toD3LineChartJson(map, getYearRange()).toString();
    }

    public void setParameters(Model model, String arguments) throws Exception {
        try {
            this.model = model;
            String[] args = arguments.split(" ");
            String years[] = args[0].split("-");
            this.startYear = Integer.parseInt(years[0]);
            this.endYear = Integer.parseInt(years[1]);
            this.seriesType = StringUtil.parseString(args[1]);
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private Map<String, Map<Integer, Integer>> getVenueMap() {
        Map<String, Map<Integer, Integer>> map = new HashMap<>();
        Map<String, String> actualNameMap = new HashMap<>();

        for (Paper paper : model.getPapers()) {
            actualNameMap.put(paper.getVenue().toLowerCase(), paper.getVenue());
            addToMap(map, paper);
        }
        mapKeyToActualName(map, actualNameMap);

        return map;
    }

    private Map<String, Map<Integer, Integer>> getAuthorMap() {
        Map<String, Map<Integer, Integer>> map = new HashMap<>();

        for (Author author : model.getAuthors()) {
            addToMap(map, author);
        }

        return map;
    }

    private void addToMap(Map<String, Map<Integer, Integer>> map, Paper paper) {
        if (map.containsKey(paper.getVenue().toLowerCase())) {
            Map<Integer, Integer> yearMap = map.get(paper.getVenue().toLowerCase());
            if (isYearInRange(paper.getYear())) {
                int count = yearMap.containsKey(paper.getYear()) ? yearMap.get(paper.getYear()) : 0;
                yearMap.put(paper.getYear(), count + 1);
            }
        } else {
            Map<Integer, Integer> yearMap = new HashMap<>();
            yearMap.put(paper.getYear(), 1);
            map.put(paper.getVenue().toLowerCase(), yearMap);
        }
    }

    private void addToMap(Map<String, Map<Integer, Integer>> map, Author author) {
        Map<Integer, Integer> yearMap = new HashMap<>();
        for (Paper paper : author.getPapers()) {
            if (isYearInRange(paper.getYear())) {
                int count = yearMap.containsKey(paper.getYear()) ? yearMap.get(paper.getYear()) : 0;
                yearMap.put(paper.getYear(), count + 1);
            }
        }
        map.put(author.getName(), yearMap);
    }

    private boolean isYearInRange(int year) {
        return year >= startYear && year <= endYear;
    }

    private int[] getYearRange() {
        return IntStream.rangeClosed(startYear, endYear).toArray();
    }

    private void filterTrivialData(Map<String, Map<Integer, Integer>> map) {
        // Remove series with no name
        map.remove("");

        // Filter series with few data
        int yearRange = endYear - startYear + 1;
        map.values().removeIf(yearMap -> seriesTotal(yearMap) < yearRange);
    }

    private int seriesTotal(Map<Integer, Integer> yearMap) {
        int total = 0;
        for (int count : yearMap.values()) {
            total += count;
        }
        return total;
    }

    // Is there a better way to re-map the keys?
    private void mapKeyToActualName(Map<String, Map<Integer, Integer>> map, Map<String, String> actualNameMap) {
        for (String name : actualNameMap.keySet()) {
            if (map.containsKey(name)) {
                map.put(actualNameMap.get(name), map.get(name));
                map.remove(name);
            }
        }
    }
}

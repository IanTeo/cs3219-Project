package logic.command;

import model.Model;
import model.Paper;
import util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class TrendCommand implements Command{
    public static final String COMMAND_WORD = "trend";
    public static final String HELP = "Error: %s\nUsage: trend [startYear]-[endYear] [type]\n" +
            "This command returns a JSON file representing the venue/author/paper trend for the specified year range";
    private Model model;
    private int startYear;
    private int endYear;
    private String type;

    public String execute() {
        // Map of (Venue Name -> Map of (Year -> Paper Count))
        Map<String, Map<Integer, Integer>> map = new HashMap<>();
        for (Paper paper : model.getPapers()) {
            addToMap(map, paper);
        }

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
            this.type = args[1];
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private void addToMap(Map<String, Map<Integer, Integer>> map, Paper paper) {
        if (map.containsKey(paper.getVenue())) {
            Map<Integer, Integer> yearMap = map.get(paper.getVenue());
            if (isYearInRange(paper.getYear())) {
                int count = yearMap.containsKey(paper.getYear()) ? yearMap.get(paper.getYear()) : 0;
                yearMap.put(paper.getYear(), count + 1);
            }
        } else {
            Map<Integer, Integer> yearMap = new HashMap<>();
            yearMap.put(paper.getYear(), 1);
            map.put(paper.getVenue(), yearMap);
        }
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
        ArrayList<String> seriesToRemove = new ArrayList<>();
        for (String series : map.keySet()) {
            Map<Integer, Integer> yearMap = map.get(series);
            int total = 0;
            for (int count : yearMap.values()) {
                total += count;
            }
            if (total < yearRange) {
                seriesToRemove.add(series);
            }
        }


        for (String series : seriesToRemove) {
            map.remove(series);
        }
    }
}

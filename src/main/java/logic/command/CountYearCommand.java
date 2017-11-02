package logic.command;

import model.Paper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Model;
import util.StringUtil;

import java.util.Map;

public class CountYearCommand implements Command{
    public static final String COMMAND_WORD = "countyear";
    public static final String HELP = "Error: %s\nUsage: countyear [start year]-[end year] [venue]\n" +
            "This command returns a JSON file representing the number of papers per year for the specified venue";
    private Model model;
    private int startYear, endYear;
    private String venue;

    public String execute() {
        try {
            int[] yearCounts = countCitationsByYear();
            JSONArray array = new JSONArray();
            for (int i = 0; i < yearCounts.length; i++) {
                JSONObject object = new JSONObject();
                object.put("year", startYear + i);
                object.put("count", yearCounts[i]);
                array.add(object);
            }
            return array.toString();
        } catch (Exception e) {
            return String.format(HELP,"Invalid year");
        }
    }

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception {
        if (!containExpectedArguments(argumentMap)) {
            throw new Exception(String.format(HELP, "Invalid Arguments"));
        }

        this.model = model;
        String years[] = argumentMap.get("year").split("-");
        this.startYear = Integer.parseInt(years[0]);
        this.endYear = Integer.parseInt(years[1]);
        this.venue = argumentMap.get("venue");
    }

    private boolean containExpectedArguments(Map<String, String> argumentMap) {
        return argumentMap.containsKey("year")
                && argumentMap.containsKey("venue");
    }

    private int[] countCitationsByYear() {
        int[] citationCounts = new int[endYear - startYear + 1];

        model.getPapers().stream()
                .filter(this::matchVenueAndYear)
                .forEach(paper -> citationCounts[paper.getYear() - startYear] += 1);

        return citationCounts;
    }

    private boolean matchVenueAndYear(Paper paper) {
        return StringUtil.containsIgnoreCase(paper.getVenue(), venue)
                && paper.getYear() >= startYear && paper.getYear() <= endYear;
    }
}

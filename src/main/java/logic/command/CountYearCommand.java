package logic.command;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Model;
import util.StringUtil;

public class CountYearCommand implements Command{
    public static final String COMMAND_WORD = "countyear";
    public static final String HELP = "Error: %s\nUsage: countyear [start year]-[end year] [venue]\n" +
            "This command returns a JSON file representing the number of papers per year for the specified venue";
    private Model model;
    private int startYear, endYear;
    private String venue;

    public String execute() {
        try {
            int[] yearCounts = countCitationsByYear(startYear, endYear);
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

    public void setParameters(Model model, String arguments) throws Exception {
        try {
            this.model = model;
            String[] args = arguments.split(" ");
            String years[] = args[0].split("-");
            this.startYear = Integer.parseInt(years[0]);
            this.endYear = Integer.parseInt(years[1]);
            this.venue = StringUtil.parseString(args[1]);
        } catch (Exception e) {
            throw new Exception(String.format(HELP, "Error parsing parameters"));
        }
    }

    private int[] countCitationsByYear(int start, int end) {
        int[] citationCounts = new int[end - start + 1];

        model.getPapers().stream()
                .filter(paper -> paper.getVenue().contains(venue) && paper.getYear() >= start && paper.getYear() <= end)
                .forEach(paper -> citationCounts[paper.getYear() - start] += 1);

        return citationCounts;
    }
}

package logic.command;

import model.Model;
import model.Paper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.StringUtil;

import java.util.Collection;

public class CountYearCommand implements Command{
    public static final String COMMAND_WORD = "countyear";
    private Model model;
    private String startYear, endYear, venue;

    public String execute() {
        try {
            int start = Integer.parseInt(startYear);
            int end = Integer.parseInt(endYear);
            int[] yearCounts = countCitationsByYear(start, end);
            JSONArray array = new JSONArray();
            for (int i = 0; i < yearCounts.length; i++) {
                JSONObject object = new JSONObject();
                object.put("year", start + i);
                object.put("count", yearCounts[i]);
                array.add(object);
            }
            return array.toString();
        } catch (Exception e) {
            return "Invalid year";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        String[] args = arguments.split(" ");
        String years[] = args[0].split("-");
        this.startYear = years[0];
        this.endYear = years[1];
        this.venue = StringUtil.parseString(args[1]);
    }

    private int[] countCitationsByYear(int start, int end) {
        int[] citationCounts = new int[end - start + 1];
        
        Collection<Paper> paperList = model.getPapers();
        for (Paper p : paperList) {
            if (p.getVenue().contains(venue) && !p.getInCitation().isEmpty()) {
                if (p.getYear() >= start && p.getYear() <= end) {
                    int index = p.getYear() - start;
                    citationCounts[index] += 1;
                }
            }
        }
        return citationCounts;
    }
}

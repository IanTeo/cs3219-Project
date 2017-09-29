package logic.command;

import model.Model;
import model.Paper;
import util.StringUtil;

import java.util.Collection;

public class CountYearCommand implements Command{
    public static final String COMMAND_WORD = "countyear";
    private Model model;
    private String startYear, endYear;

    public String execute() {
        try {
            int start = Integer.parseInt(startYear);
            int end = Integer.parseInt(endYear);
            return StringUtil.formatYearCountReply(countCitationsByYear(start, end), start);
        } catch (Exception e) {
            return "Invalid year";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        String years[] = arguments.split("-");
        this.startYear = years[0];
        this.endYear = years[1];
    }

    private int[] countCitationsByYear(int start, int end) {
        int[] citationCounts = new int[end - start + 1];
        
        Collection<Paper> paperList = model.getPapers();
        for (Paper p : paperList) {
            if (p.getInCitation().size() > 0) {
                if (p.getDate() >= start && p.getDate() <= end) {
                    int index = p.getDate() - start;
                    citationCounts[index] = citationCounts[index] + 1;
                }
            }
        }
        return citationCounts;
    }
}

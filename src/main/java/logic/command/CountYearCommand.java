package logic.command;

import model.Model;
import model.Paper;

import java.util.Collection;

public class CountYearCommand implements Command{
    public static final String COMMAND_WORD = "countyear";
    private Model model;
    private String startYear, endYear;

    public String execute() {
        try {
            int start = Integer.parseInt(startYear);
            int end = Integer.parseInt(endYear);
            return countCitationsInYear(start, end);
        } catch (Exception e) {
            return "Invalid year";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        String years[] = arguments.split(" ");
        this.startYear = years[0];
        this.endYear = years[1];
    }

    private String countCitationsInYear(int start, int end) {
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
        return formatReply(citationCounts, start);
    }
    
    private String formatReply(int[] citationCounts, int start) {
        StringBuilder builder = new StringBuilder();
        builder.append(start).append(": ").append(citationCounts[0]);
        for (int i = 1; i < citationCounts.length; i++) {
            builder.append(String.format(", %s: %s", start + i, citationCounts[i]));
        }
        return builder.toString();
    }
}

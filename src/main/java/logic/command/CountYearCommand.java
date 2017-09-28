package logic.command;

import model.Model;
import model.Paper;

import java.util.Collection;
import java.util.HashSet;

public class CountYearCommand implements Command{
    public static final String COMMAND_WORD = "countyear";
    private Model model;
    private String yearString;

    public String execute() {
        try {
            int year = Integer.parseInt(yearString);
            return "" + countCitationsInYear(year);
        } catch (Exception e) {
            return "Invalid year";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.yearString = arguments;
    }

    private int countCitationsInYear(int year) {
        Collection<Paper> paperList = model.getPapers();
        int count = 0;
        for (Paper p : paperList) {
            if (p.getInCitation().size() > 0) {
                if (p.getDate() == year) {
                    count++;
                }
            }
        }
        return count;
    }
}

package logic.command;

import model.Model;
import model.Paper;
import util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;

public class CountAuthorCommand implements Command{
    public static final String COMMAND_WORD = "countauthor";
    private Model model;
    private String[] authorNames;
    private String startYear, endYear;

    public String execute() {
        try {
            int start = Integer.parseInt(startYear);
            int end = Integer.parseInt(endYear);
            return StringUtil.formatYearCountReply(countAuthorByYear(start, end), start);
        } catch (Exception e) {
            return "Invalid year";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        
        int dateLastIndex = arguments.indexOf(' ');
        String years[] = arguments.substring(0, dateLastIndex).trim().split("-");
        this.startYear = years[0];
        this.endYear = years[1];
        this.authorNames = arguments.substring(dateLastIndex).trim().toLowerCase().split(",");
    }

    private int[] countAuthorByYear(int start, int end) {
        int[] authorCounts = new int[end - start + 1];
        
        Collection<Paper> paperList = model.getPapers();
        for (Paper p : paperList) {
            if (!p.getInCitation().isEmpty() && hasAuthor(p)) {
                int index = p.getYear() - start;
                if (index >= 0 && index < authorCounts.length) {
                    authorCounts[index] += p.getInCitation().size();
                }
            }
        }
        return authorCounts;
    }
    
    private boolean hasAuthor(Paper paper) {
        for (String authorName : authorNames) {
            if (paper.hasAuthor(authorName.trim())) {
                return true;
            }
        }
        return false;
    }
}

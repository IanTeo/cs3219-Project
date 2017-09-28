package logic.command;

import model.Model;
import model.Paper;

import java.util.Collection;
import java.util.HashSet;

public class CountCommand implements Command{
    public static final String COMMAND_WORD = "count";
    private Model model;
    private String type;

    public String execute() {
        int count;
        switch (type) {
            case "dataset" :
                count = countNumberOfDataset();
                break;

            case "citation" :
                count = countTotalNumberOfCitations();
                break;

            case "unique citation" :
                count = countUniqueNumberOfCitations();
                break;
                
            case "citation author" :
                count = countCitationAuthors();
                break;
                
            case "citation range" :
                count = countCitationRangeOfYears();
                break;

            default :
                count = -1;
                break;
        }
        if (count == -1) return "Invalid arguments";
        return "" + count;
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.type = arguments.toLowerCase();
    }

    private int countNumberOfDataset() {
        return model.getNumDataSet();
    }

    private int countTotalNumberOfCitations() {
        Collection<Paper> paperList = model.getPapers();
        int count = 0;
        for (Paper p : paperList) {
            count += p.getOutCitation().size();
        }
        return count;
    }

    private int countUniqueNumberOfCitations() {
        Collection<Paper> paperList = model.getPapers();
        int count = 0;
        for (Paper p : paperList) {
            if (p.getInCitation().size() > 0) {
                count++;
            }
        }
        return count;
    }
    
    private int countCitationAuthors() {
        Collection<Paper> paperList = model.getPapers();
        HashSet<String> authors = new HashSet<>();
        for (Paper p : paperList) {
            if (p.getInCitation().size() > 0) {
                for (String author : p.getAuthors()) {
                    authors.add(author);
                }
            }
        }
        return authors.size();
    }
    
    private int countCitationRangeOfYears() {
        Collection<Paper> paperList = model.getPapers();
        int minYear = -1, maxYear = -1;
        for (Paper p : paperList) {
            if (p.getInCitation().size() > 0) {
                int year = p.getDate();
                if (year == 0) break;
                if (minYear == -1 || minYear > year) {
                    minYear = year;
                }
                if (maxYear == -1 || maxYear < year) {
                    maxYear = year;
                }
            }
        }
        return (maxYear - minYear) + 1;
    }
}

package logic.command;

import model.Model;
import model.Paper;

import java.util.Collection;

public class CountConferenceCommand implements Command{
    public static final String COMMAND_WORD = "countconference";
    private Model model;
    private String[] conferenceNames;

    public String execute() {
        return "" + countCitationsByConference();
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.conferenceNames = arguments.toLowerCase().split(",");
    }

    private int countCitationsByConference() {
        Collection<Paper> paperList = model.getPapers();
        int count = 0;
        for (Paper p : paperList) {
            if (!p.getInCitation().isEmpty() && hasConferenceReference(p)) {
                count += p.getInCitation().size();
            }
        }
        return count;
    }
    
    private boolean hasConferenceReference(Paper paper) {
        for (String conferenceName : conferenceNames) {
            if (paper.getRawString().contains(conferenceName)) {
                return true;
            }
        }
        return false;
    }
}

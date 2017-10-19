package logic.command;

import model.Model;
import model.Paper;
import util.StringUtil;

import java.util.Collection;
import java.util.stream.Collectors;

public class CitationCommand implements Command{
    public static final String COMMAND_WORD = "citation";
    private Model model;
    private String citationType;
    private String paperId;

    public String execute() {
        Paper paper = model.getPaperById(paperId);
        // Try to get paper by name if no paper found
        if (paper == null) {
            paper = model.getPaperByName(paperId);
        }
        // If still no paper found, paper does not exist
        if (paper == null) {
            return "Paper not found";
        }

        switch (citationType) {
            case "in" :
                return getCitations(paper.getInCitation());

            case "out" :
                return getCitations(paper.getOutCitation());

            default :
                return "Invalid citation type";
        }
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        int citationTypeLastIndex = arguments.indexOf(' ');
        this.citationType = StringUtil.parseString(arguments.substring(0, citationTypeLastIndex));
        this.paperId = StringUtil.parseString(arguments.substring(citationTypeLastIndex));
    }

    private String getCitations(Collection<Paper> citationList) {
        return citationList.stream().map(Paper::getId).collect(Collectors.toList()).toString();
    }
}

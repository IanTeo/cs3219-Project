package logic.command;

import model.Model;
import model.Paper;
import util.StringUtil;

import java.util.Map;

public class DetailCommand implements Command{
    public static final String COMMAND_WORD = "detail";
    public static final String HELP = "Error: %s\nUsage: detail [paper name/id]\n" +
            "Debugging command to view details on specified paper";
    private Model model;
    private String paperId;

    public String execute() {
        Paper paper = model.getPaper(paperId);
        if (paper == null) {
            return String.format(HELP, "Paper not found");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(paper);
        builder.append("======== IN CITATION ========\n");
        for (Paper p : paper.getInCitation()) {
            builder.append(p).append("\n");
        }
        builder.append("======== OUT CITATION ========\n");
        for (Paper p : paper.getOutCitation()) {
            builder.append(p).append("\n");
        }
        return builder.toString();
    }

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception {
        if (!containExpectedArguments(argumentMap)) {
            throw new Exception(String.format(HELP, "Invalid Arguments"));
        }

        this.model = model;
        this.paperId = StringUtil.sanitise(argumentMap.get("paper"));
    }

    private boolean containExpectedArguments(Map<String, String> argumentMap) {
        return argumentMap.containsKey("paper");
    }
}

package logic.command;

import model.Model;
import model.Paper;
import util.StringUtil;

public class DetailCommand implements Command{
    public static final String COMMAND_WORD = "detail";
    private Model model;
    private String paperName;

    public String execute() {
        Paper paper = model.getPaper(paperName);
        if (paper == null) {
            return "Paper not found";
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

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.paperName = StringUtil.parseString(arguments);
    }
}

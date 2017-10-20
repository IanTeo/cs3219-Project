package logic.command;

import model.Model;
import model.Paper;
import util.StringUtil;

public class PaperCommand implements Command{
    public static final String COMMAND_WORD = "paper";
    private Model model;
    private String paperId;

    public String execute() {
        Paper paper = model.getPaper(paperId);
        if (paper == null) {
            return "Paper not found";
        }

        return paper.toString();
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.paperId = StringUtil.parseString(arguments);
    }
}

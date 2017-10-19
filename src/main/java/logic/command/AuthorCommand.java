package logic.command;

import model.Author;
import model.Model;
import util.StringUtil;

public class AuthorCommand implements Command{
    public static final String COMMAND_WORD = "author";
    private Model model;
    private String authorId;

    public String execute() {
        Author author = model.getAuthor(authorId);
        if (author == null) {
            return "Author not found";
        }

        return author.toString();
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
        this.authorId = StringUtil.parseString(arguments);
    }
}

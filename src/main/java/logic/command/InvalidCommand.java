package logic.command;

import model.Model;

public class InvalidCommand implements Command {
    public String execute() {
        return "Invalid Command";
    }

    public void setParameters(Model mode, String arguments) {
        // Am I breaking LSP?
    }
}

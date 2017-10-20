package logic.command;

import model.Model;

public class InvalidCommand implements Command {
    private String message;

    public InvalidCommand(String message) {
        this.message = message;
    }
    public String execute() {
        return message;
    }

    public void setParameters(Model mode, String arguments) {
        // Am I breaking LSP?
    }
}

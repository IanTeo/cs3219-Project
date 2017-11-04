package logic.command;

import model.Model;

import java.util.Map;

public class InvalidCommand implements Command {
    private String message;

    public InvalidCommand(String message) {
        this.message = message;
    }
    public String execute() {
        return message;
    }

    public void setParameters(Model mode, Map<String, String> argumentMap) {
        // Am I breaking LSP?
    }
}

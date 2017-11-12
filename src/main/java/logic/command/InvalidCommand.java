package logic.command;

import model.Model;

import java.util.Map;

import logic.jsonconverter.JsonConverter;

public class InvalidCommand implements Command {
    private String message;

    public InvalidCommand(String message) {
        this.message = message;
    }
    public String execute() {
        return JsonConverter.errorMessageToJson(message).toString();
    }

    public void setParameters(Model model, Map<String, String> paramMap) {
        // Am I breaking LSP?
    }
}

package logic.command;

import logic.jsonconverter.JsonConverter;

public class InvalidCommand implements Command {
    private String message;

    public InvalidCommand(String message) {
        this.message = message;
    }
    public String execute() {
        return JsonConverter.errorMessageToJson(message).toString();
    }
}

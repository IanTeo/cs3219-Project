package logic;

import logic.command.Command;
import logic.command.CommandParser;
import model.Model;

public class Controller {
    private CommandParser commandParser;

    public Controller(Model model) {
        this.commandParser = new CommandParser(model);
    }

    public String executeQuery(String query) {
        Command command = commandParser.parseCommand(query);
        return command.execute();
    }
}

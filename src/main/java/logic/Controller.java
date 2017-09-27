package logic;

import logic.command.Command;
import logic.command.CommandParser;
import logic.parser.FileParserManager;
import model.Model;

public class Controller {
    private Model model;
    private CommandParser commandParser;

    public Controller(Model model) {
        this.model = model;
        this.commandParser = new CommandParser(model);
    }

    public String executeQuery(String query) {
        Command command = commandParser.parseCommand(query);
        return command.execute();
    }
}

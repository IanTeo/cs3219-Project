package logic;

import logic.command.Command;
import logic.command.CommandParser;
import logic.command.LoadCommand;
import logic.exception.ParseException;
import model.Model;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Model model;
    private CommandParser commandParser;

    public Controller(Model model) {
        this.model = model;
        this.commandParser = new CommandParser(model);
    }

    public String executeQuery(Map<String, String> queryMap) {
        Command command = commandParser.parseCommand(queryMap);
        return command.execute();
    }

    public String loadData(String directory) throws ParseException {
        Map<String, String> argumentMap = new HashMap<>();
        argumentMap.put("directory", directory);
        Command command = new LoadCommand();
        command.setParameters(model, argumentMap);
        return command.execute();
    }
}

package logic;

import logic.command.Command;
import logic.command.CommandParser;
import logic.command.LoadCommand;
import logic.command.TopCommand;
import model.Model;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    private CommandParser commandParser;
    private Model model;

    public Controller(Model model) {
        this.model = model;
        this.commandParser = new CommandParser(model);
    }

    public String loadData(String directory) throws Exception {
        Map<String, String> argumentMap = new HashMap<>();
        argumentMap.put("directory", directory);

        Command command = new LoadCommand();
        command.setParameters(model, argumentMap);
        return command.execute();
    }

    public String executeQuery(Map<String, String> queryMap) throws Exception {
        Command command = commandParser.parseCommand(queryMap);
        return command.execute();
    }

    public String executeTopQuery() {
        Command command = new TopCommand();
        return "";
    }
}

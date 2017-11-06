package logic;

import logic.command.Command;
import logic.command.CommandParser;
import logic.parser.FileParserManager;
import model.Model;

import java.util.Map;

public class Controller {
    public static final String DATA_URL = "Data/";

    private Model model;
    private CommandParser commandParser;

    public Controller(Model model) {
        this.model = model;
        this.commandParser = new CommandParser(model);

        loadData();
    }

    public String executeQuery(Map<String, String> queryMap) {
        Command command = commandParser.parseCommand(queryMap);
        return command.execute();
    }

    private void loadData() {
        model.clear();
        FileParserManager fileParserManager = new FileParserManager(model);
        try {
            fileParserManager.parseFilesInDirectory(DATA_URL);
        } catch (Exception e) {
            model.clear();
            e.printStackTrace();
        }
    }
}

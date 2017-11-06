package logic.command;

import java.util.Map;

import logic.exception.ParseException;
import logic.parser.TrendCommandParser;
import model.Model;

public class CommandParser {
    private Model model;

    public CommandParser(Model model) {
        this.model = model;
    }

    public Command parseCommand(Map<String, String> queryMap) {
        Command command;
        String commandWord = queryMap.get("command");

        try {
            command = parseCommand(commandWord, queryMap);
            command.setParameters(model, queryMap);
        } catch (Exception e) {
            command = new InvalidCommand(e.getMessage());
        }
        return command;
    }

    private Command parseCommand(String commandWord, Map<String, String> queryMap) throws ParseException {
        Command command;
        switch (commandWord) {
            case TopCommand.COMMAND_WORD :
                command = new TopCommand();
                break;

            case WebCommand.COMMAND_WORD :
                command = new WebCommand();
                break;

            case WordCommand.COMMAND_WORD :
                command = new WordCommand();
                break;

            case TrendCommand.COMMAND_WORD:
                command = new TrendCommandParser().parse(queryMap);
                break;

            default :
                command = new InvalidCommand("Invalid command");
                break;
        }
        return command;
    }
}

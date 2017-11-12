package logic.command;

import java.util.Map;

import logic.exception.ParseException;
import logic.parser.TopCommandParser;
import logic.parser.TrendCommandParser;
import logic.parser.WebCommandParser;
import logic.parser.WordCommandParser;
import model.Model;

public class CommandParser {
    private Model model;

    public CommandParser(Model model) {
        this.model = model;
    }

    public Command parseCommand(Map<String, String> queryMap) {
        String commandWord = queryMap.get("command");
        
        try {
            return parseCommand(commandWord, queryMap);
        } catch (ParseException e) {
            return new InvalidCommand(e.getMessage());
        }
    }
    
    public Command parseCommand(String commandWord, Map<String, String> queryMap) throws ParseException {
        Command command;
        switch (commandWord) {
            case TopCommand.COMMAND_WORD :
                command = new TopCommandParser().parse(model, queryMap);
                break;

            case WebCommand.COMMAND_WORD :
                command = new WebCommandParser().parse(model, queryMap);
                break;

            case WordCommand.COMMAND_WORD :
                command = new WordCommandParser().parse(model, queryMap);
                break;

            case TrendCommand.COMMAND_WORD:
                command = new TrendCommandParser().parse(model, queryMap);
                break;

            default :
                command = new InvalidCommand("Invalid command");
                break;
        }
        return command;
    }
}

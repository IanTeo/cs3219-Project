package logic.command;

import logic.exception.ParseException;
import logic.parser.TrendCommandParser;
import model.Model;

import java.util.Map;

public class CommandParser {
    private Model model;

    public CommandParser(Model model) {
        this.model = model;
    }

    public Command parseCommand(Map<String, String> queryMap) {
        Command command;
        String commandWord = queryMap.get("command");

        try {
            command = parseCommand(commandWord);
        } catch (ParseException pe) {
            command = new InvalidCommand(pe.getMessage());
        }

        try {
            command.setParameters(model, queryMap);
        } catch (Exception e) {
            command = new InvalidCommand(e.getMessage());
        }
        return command;
    }

    private Command parseCommand(String commandWord) throws ParseException {
        Command command;
        switch (commandWord) {
            case CountYearCommand.COMMAND_WORD :
                command = new CountYearCommand();
                break;

            case LoadCommand.COMMAND_WORD :
                command = new LoadCommand();
                break;

            case TopCommand.COMMAND_WORD :
                command = new TopCommand();
                break;

            case VenueCommand.COMMAND_WORD :
                command = new VenueCommand();
                break;

            case WebCommand.COMMAND_WORD :
                command = new WebCommand();
                break;

            case WordCommand.COMMAND_WORD :
                command = new WordCommand();
                break;

            /*case TrendCommand.COMMAND_WORD:
                command = new TrendCommand();
                break;*/

            default :
                command = new InvalidCommand("Invalid command");
                break;
        }
        return command;
    }
}

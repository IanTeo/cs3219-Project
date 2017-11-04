package logic.command;

import logic.exception.ParseException;
import model.Model;

public class CommandParser {
    private Model model;

    public CommandParser(Model model) {
        this.model = model;
    }

    public Command parseCommand(String query) {
        Command command;
        int commandWordLastIndex = query.indexOf(' ');
        String commandWord = query;
        String arguments = "";

        if (commandWordLastIndex != -1) {
            commandWord = query.substring(0, commandWordLastIndex).trim();
            arguments = query.substring(commandWordLastIndex).trim();
        }

        try {
            command = parseCommand(commandWord, arguments);
        } catch (ParseException pe) {
            command = new InvalidCommand(pe.getMessage());
        }

        try {
            command.setParameters(model, arguments);
        } catch (Exception e) {
            command = new InvalidCommand(e.getMessage());
        }
        return command;
    }

    private Command parseCommand(String commandWord, String arguments) throws ParseException {
        Command command;
        switch (commandWord) {
            case CountYearCommand.COMMAND_WORD :
                command = new CountYearCommand();
                break;

            case DetailCommand.COMMAND_WORD :
                command = new DetailCommand();
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

            case TrendCommand.COMMAND_WORD:
                command = new TrendCommand();
                break;

            default :
                command = new InvalidCommand("Invalid command");
                break;
        }
        return command;
    }
}

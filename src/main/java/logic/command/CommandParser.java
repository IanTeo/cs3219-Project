package logic.command;

import model.Model;

import java.util.Map;

public class CommandParser {
    private Model model;

    public CommandParser(Model model) {
        this.model = model;
    }

    public Command parseCommand(Map<String, String> queryMap) throws Exception {
        Command command;

        switch (queryMap.get("query")) {
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

            default :
                command = new InvalidCommand("Invalid command");
                break;
        }
        command.setParameters(model, queryMap);

        return command;
    }
}

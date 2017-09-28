package logic.command;

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
            arguments = query.substring(commandWordLastIndex, query.length()).trim();
        }

        switch (commandWord) {
            case CountCommand.COMMAND_WORD :
                command = new CountCommand();
                break;
                
            case CountYearCommand.COMMAND_WORD :
                command = new CountYearCommand();
                break;
                
            case CountConferenceCommand.COMMAND_WORD :
                command = new CountConferenceCommand();
                break;

            case DetailCommand.COMMAND_WORD :
                command = new DetailCommand();
                break;

            case LoadCommand.COMMAND_WORD :
                command = new LoadCommand();
                break;

            default :
                command = new InvalidCommand();
                break;
        }

        command.setParameters(model, arguments);
        return command;
    }
}

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
            arguments = query.substring(commandWordLastIndex).trim();
        }

        switch (commandWord) {
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

            case TopCommand.COMMAND_WORD :
                command = new TopCommand();
                break;

            case AuthorCommand.COMMAND_WORD :
                command = new AuthorCommand();
                break;

            case PaperCommand.COMMAND_WORD :
                command = new PaperCommand();
                break;

            case CitationCommand.COMMAND_WORD :
                command = new CitationCommand();
                break;

            default :
                command = new InvalidCommand();
                break;
        }

        command.setParameters(model, arguments);
        return command;
    }
}

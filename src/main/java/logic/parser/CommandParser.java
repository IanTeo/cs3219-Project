package logic.parser;

import logic.command.Command;
import logic.exception.ParseException;

public interface CommandParser {
    Command parse(String arguments) throws ParseException;
}

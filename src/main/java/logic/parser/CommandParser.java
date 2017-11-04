package logic.parser;

import logic.command.Command;
import logic.exception.ParseException;

import java.util.Map;

public interface CommandParser {
    Command parse(Map<String, String> argumentMap) throws ParseException;
}

package logic.command;

import org.junit.Test;
import util.ModelStub;

import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

public class CommandParserTest {
    CommandParser commandParser = new CommandParser(new ModelStub());

    @Test
    public void parseCommand_invalidCommandWord_returnInvalidCommand() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("command", "invalid");

        Command command = commandParser.parseCommand(queryMap);
        assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void parseCommand_missingCommandArguments_returnInvalidCommand() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("command", "top");

        Command command = commandParser.parseCommand(queryMap);
        assertTrue(command instanceof InvalidCommand);
    }


    @Test
    public void parseCommand_TopCommandWord_returnTopCommand() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("command", "top");
        queryMap.put("count", "2");
        queryMap.put("category", "venue");
        queryMap.put("measure", "paper");

        Command command = commandParser.parseCommand(queryMap);
        assertTrue(command instanceof TopCommand);
    }


    @Test
    public void parseCommand_TrendCommandWord_returnTrendCommand() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("command", "trend");
        queryMap.put("measure", "paper");

        Command command = commandParser.parseCommand(queryMap);
        assertTrue(command instanceof TrendCommand);
    }

    @Test
    public void parseCommand_WebCommandWord_returnWebCommand() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("command", "web");
        queryMap.put("level", "4");
        queryMap.put("paper", "P1");

        Command command = commandParser.parseCommand(queryMap);
        assertTrue(command instanceof WebCommand);
    }

    @Test
    public void parseCommand_WordCommandWord_returnWordCommand() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("command", "word");
        queryMap.put("category", "paper");

        Command command = commandParser.parseCommand(queryMap);
        assertTrue(command instanceof WordCommand);
    }
}

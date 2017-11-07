package logic.command;

import logic.exception.ParseException;
import model.Model;
import org.junit.Before;
import org.junit.Test;
import util.FileReader;
import util.ModelStub;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TopCommandTest {
    private Model model;

    @Before
    public void initModel() {
        model = new ModelStub();
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingTypeArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("venue", "icse");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingCountArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type", "paper");
        paramMap.put("venue", "icse");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingVenueArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("type", "paper");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);
    }

    @Test
    public void execute_invalidTypeArgument_printHelp() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("type", "invalid");
        paramMap.put("venue", "icse");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = String.format(command.HELP, "Invalid type");
        assertEquals(actual, expected);
    }

    @Test
    public void execute_venueNotFound_printEmptyJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("type", "paper");
        paramMap.put("venue", "invalid");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = "[]";
        assertEquals(actual, expected);
    }

    @Test
    public void execute_validTypeTitleAndNoVenue_printJsonCountPaper() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "100");
        paramMap.put("type", "paper");
        paramMap.put("venue", "");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile("Top_ValidPaperResult.json");
        assertEquals(actual, expected);
    }

    @Test
    public void execute_validTypeAuthor_printJsonCountAuthor() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "1");
        paramMap.put("type", "author");
        paramMap.put("venue", "icse");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile("Top_ValidAuthorResult.json");
        assertEquals(actual, expected);
    }
}

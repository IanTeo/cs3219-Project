package logic.command;

import static org.junit.Assert.assertEquals;

import logic.exception.ParseException;
import model.Model;
import org.junit.Before;
import org.junit.Test;

import model.ModelManager;
import model.Paper;
import util.FileReader;
import util.ModelStub;
import util.PaperBuilder;

import java.util.HashMap;
import java.util.Map;

public class WordCommandTest {
    private Model model;

    @Before
    public void initModel() {
        model = new ModelStub();
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingTypeArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "3");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingCountArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type", "title");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);
    }

    @Test
    public void execute_invalidTypeArgumentDefaultToTitle_printJsonCountTitle() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "4");
        paramMap.put("type", "invalid");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile("Word_ValidTitleResult.json");
        assertEquals(actual, expected);
    }

    @Test
    public void execute_validTypeTitleAndLargetCount_printJsonCountTitle() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "100");
        paramMap.put("type", "title");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile("Word_ValidTitleResultLarge.json");
        assertEquals(actual, expected);
    }

    @Test
    public void execute_validTypeVenue_printJsonCountVenue() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("type", "venue");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile("Word_ValidVenueResult.json");
        assertEquals(actual, expected);
    }
}

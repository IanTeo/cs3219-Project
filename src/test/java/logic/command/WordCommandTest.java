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
    private Model model = new ModelStub();
    private static String BASE_URL = "WordCommandTest/%s";


    @Test(expected = ParseException.class)
    public void setParameter_noCategoryArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_invalidCategoryType_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type", "invalid");

        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);
    }

    @Test
    public void execute_validCategoryPaper_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("category", "paper");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidPaperResult.json"));

        paramMap.put("ignore", "VENUE");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidPaperWithFilterResult.json"));
    }

    @Test
    public void execute_validCategoryVenue_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("category", "venue");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidVenueResult.json"));

        paramMap.put("ignore", "arXiv");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidVenueWithFilterResult.json"));
    }

    @Test
    public void execute_validCategoryAuthor_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "author");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidAuthorResult.json"));

        paramMap.put("ignore", "author,PaperS");
        assertCommand(paramMap, String.format(BASE_URL, "Word_ValidAuthorWithFilterResult.json"));
    }

    private void assertCommand(Map<String, String> paramMap, String expectedOutputFileName) throws Exception {
        WordCommand command = new WordCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile(expectedOutputFileName);
        assertEquals(expected, actual);
    }
}

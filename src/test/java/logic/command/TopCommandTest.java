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
    private Model model = new ModelStub();
    private static String BASE_URL = "TopCommandTest/%s";

    @Test(expected = ParseException.class)
    public void setParameter_missingTypeArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("category", "paper");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingCountArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "paper");
        paramMap.put("measure", "incitation");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void setParameter_missingVenueArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("measure", "incitation");

        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);
    }

    @Test
    public void execute_validCategoryPaperAndMeasureIncitation_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("count", "100");
        paramMap.put("category", "papER");
        paramMap.put("measure", "incitation");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidPaperIncitationResult.json"));

        paramMap.put("venue", "Icse");
        paramMap.put("author", "author WiTh papers P1 P3");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidPaperIncitationFilterResult.json"));
    }

    @Test
    public void execute_validCategoryVenueAndMeasurePaper_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("count", "2");
        paramMap.put("category", "venue");
        paramMap.put("measure", "paper");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidVenuePaperResult.json"));

        paramMap.put("year", "2012-2015");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidVenuePaperFilterResult.json"));

        paramMap.put("measure", "author");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidVenueAuthorFilterResult.json"));
    }

    @Test
    public void execute_validCategoryAuthorAndMeasureOutcitation_printJson() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("count", "2");
        paramMap.put("category", "author");
        paramMap.put("measure", "outcitation");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidAuthorOutcitationResult.json"));

        paramMap.put("author", "author with papers P1 P2,author with papers P1 P3");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidAuthorOutcitationFilterResult.json"));

        paramMap.put("measure", "veNue");
        assertCommand(paramMap, String.format(BASE_URL, "Top_ValidAuthorVenueFilterResult.json"));
    }

    private void assertCommand(Map<String, String> paramMap, String expectedOutputFileName) throws Exception {
        TopCommand command = new TopCommand();
        command.setParameters(model, paramMap);

        String actual = command.execute();
        String expected = FileReader.readFile(expectedOutputFileName);
        assertEquals(expected, actual);
    }
}

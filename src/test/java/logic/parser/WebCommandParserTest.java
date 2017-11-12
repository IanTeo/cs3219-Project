package logic.parser;

import logic.command.TopCommand;
import logic.command.WebCommand;
import logic.exception.ParseException;
import logic.filter.Filter;
import logic.filter.PaperTitleFilter;
import logic.filter.PaperVenueFilter;
import logic.model.Category;
import logic.model.Measure;
import model.Model;
import model.Paper;
import org.junit.Test;
import util.ModelStub;
import util.SampleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WebCommandParserTest {
    private Model model = new ModelStub();

    @Test(expected = ParseException.class)
    public void parse_missingPaperArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");

        new WebCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_missingLevelArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("paper", "P1");

        new WebCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidLevelArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "invalid");
        paramMap.put("paper", "P1");

        new WebCommandParser().parse(model, paramMap);
    }


    @Test(expected = ParseException.class)
    public void execute_paperNotFound_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");
        paramMap.put("paper", "invalid");

        new WebCommandParser().parse(model, paramMap);
    }

    @Test
    public void parse_validArguments_validWebCommand() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("level", "4");
        paramMap.put("paper", "P1");

        int expectedLevel = 4;
        Paper expectedPaper = SampleData.PAPER_1;
        assertWebCommandEquals(paramMap, expectedLevel, expectedPaper);
    }

    private void assertWebCommandEquals(Map<String, String> paramMap, int expectedLevel, Paper expectedPaper) throws Exception {
        WebCommand expected = new WebCommand(expectedLevel, expectedPaper);
        WebCommand actual = new WebCommandParser().parse(model, paramMap);

        assertEquals(expected.level, actual.level);
        assertEquals(expected.paper, actual.paper);
    }
}

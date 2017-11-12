package logic.parser;

import logic.command.WebCommand;
import logic.command.WordCommand;
import logic.exception.ParseException;
import logic.model.Category;
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

public class WordCommandParserTest {
    private Model model = new ModelStub();

    @Test(expected = ParseException.class)
    public void parse_noCategoryArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();

        new WordCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidCategoryArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "invalid");

        new WordCommandParser().parse(model, paramMap);
    }

    @Test
    public void parse_validArguments_validWordCommand() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "paper");

        Category expectedCategory = Category.PAPER;
        List<String> expectedStopWords = new ArrayList<>();
        assertWebCommandEquals(paramMap, expectedCategory, expectedStopWords);

        paramMap.put("ignore", "test");
        expectedStopWords.add("test");
        assertWebCommandEquals(paramMap, expectedCategory, expectedStopWords);

        paramMap.put("ignore", "test,testing");
        expectedStopWords.add("test");
        expectedStopWords.add("testing");
        assertWebCommandEquals(paramMap, expectedCategory, expectedStopWords);
    }

    private void assertWebCommandEquals(Map<String, String> paramMap, Category expectedCategory,
                                        List<String> expectedStopWords) throws Exception {
        WordCommand expected = new WordCommand(model, expectedCategory, expectedStopWords);
        WordCommand actual = new WordCommandParser().parse(model, paramMap);

        assertEquals(expected.category, actual.category);
        assertEquals(expected.stopWords, actual.stopWords);
    }
}

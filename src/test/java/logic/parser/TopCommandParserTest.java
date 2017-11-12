package logic.parser;

import logic.command.TopCommand;
import logic.exception.ParseException;
import logic.filter.*;
import logic.model.Category;
import logic.model.Measure;
import logic.model.YearRange;
import model.Model;
import org.junit.Test;
import util.ModelStub;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TopCommandParserTest {
    private Model model = new ModelStub();

    @Test(expected = ParseException.class)
    public void parse_missingTypeArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("category", "paper");

        new TopCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_missingCountArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("category", "paper");
        paramMap.put("measure", "incitation");

        new TopCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_missingVenueArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "2");
        paramMap.put("measure", "incitation");

        new TopCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidCountArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "invalid");
        paramMap.put("category", "paper");
        paramMap.put("measure", "incitation");

        new TopCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidCategoryArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "3");
        paramMap.put("category", "invalid");
        paramMap.put("measure", "incitation");

        new TopCommandParser().parse(model, paramMap);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidMeasureArgument_throwsParseException() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "3");
        paramMap.put("category", "paper");
        paramMap.put("measure", "invalid");

        new TopCommandParser().parse(model, paramMap);
    }

    @Test
    public void parse_validArguments_validTopCommand() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("count", "3");
        paramMap.put("category", "paper");
        paramMap.put("measure", "author");

        int expectedCount = 3;
        Measure expectedMeasure = Measure.AUTHOR;
        Category expectedCategory = Category.PAPER;
        List<Filter> expectedFilters = new ArrayList<>();
        assertTopCommandEquals(paramMap, expectedCount, expectedMeasure, expectedCategory, expectedFilters);

        paramMap.put("venue", "foo");
        expectedFilters.add(new PaperVenueFilter("foo"));
        assertTopCommandEquals(paramMap, expectedCount, expectedMeasure, expectedCategory, expectedFilters);

        paramMap.put("paper", "bar,baz");
        expectedFilters.add(new PaperTitleFilter("bar", "baz"));
        assertTopCommandEquals(paramMap, expectedCount, expectedMeasure, expectedCategory, expectedFilters);

        paramMap.put("author", "qux");
        expectedFilters.add(new AuthorFilter("qux"));
        assertTopCommandEquals(paramMap, expectedCount, expectedMeasure, expectedCategory, expectedFilters);
        
        paramMap.put("year", "2001-2011");
        expectedFilters.add(new YearFilter(new YearRange(2001, 2011)));
        assertTopCommandEquals(paramMap, expectedCount, expectedMeasure, expectedCategory, expectedFilters);
    }

    private void assertTopCommandEquals(Map<String, String> paramMap, int expectedCount, Measure expectedMeasure,
                                        Category expectedCategory, List<Filter> expectedFilters) throws Exception {
        TopCommand expected = new TopCommand(model, expectedCount, expectedMeasure, expectedCategory, expectedFilters);
        TopCommand actual = new TopCommandParser().parse(model, paramMap);

        assertEquals(expected.category, actual.category);
        assertEquals(expected.measure, actual.measure);
        assertEquals(expected.filters, actual.filters);
    }
}

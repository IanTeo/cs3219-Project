package logic.parser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import logic.command.TrendCommand;
import logic.exception.ParseException;
import logic.filter.AuthorFilter;
import logic.filter.Filter;
import logic.filter.PaperTitleFilter;
import logic.filter.PaperVenueFilter;
import logic.filter.YearFilter;
import logic.model.Category;
import logic.model.Measure;
import logic.model.YearRange;
import model.Model;
import util.ModelStub;

public class TrendCommandParserTest {
    private Model model = new ModelStub();
    
    @Test
    public void parse_measureOnly_validTrendCommand() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("measure", "paper");

        TrendCommand expected = new TrendCommand(model, Measure.PAPER, Category.TOTAL, Collections.emptyList());
        assertTrendCommandEquals(expected, new TrendCommandParser().parse(model, arguments));
    }

    @Test
    public void parse_allParametersPresent_validTrendCommand() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("measure", "author");
        arguments.put("category", "venue");
        arguments.put("venue", "foo");
        arguments.put("paper", "bar");
        arguments.put("author", "baz qux,qux bax");
        arguments.put("year", "2000-2001");

        Collection<Filter> expectedFilters = Arrays.asList(
                new PaperVenueFilter("foo"),
                new PaperTitleFilter("bar"),
                new AuthorFilter("baz qux", "qux bax"),
                new YearFilter(new YearRange(2000, 2001)));

        TrendCommand expected = new TrendCommand(model, Measure.AUTHOR, Category.VENUE, expectedFilters);
        assertTrendCommandEquals(expected, new TrendCommandParser().parse(model, arguments));
    }

    @Test(expected = ParseException.class)
    public void parse_categoryOnly_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("category", "venue");
        new TrendCommandParser().parse(model, arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_filterOnly_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("venue", "foo");
        new TrendCommandParser().parse(model, arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidMeasure_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("measure", "invalidMeasure");
        new TrendCommandParser().parse(model, arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidCategory_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("category", "invalidCategory");
        new TrendCommandParser().parse(model, arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidYear_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("year", "invalidYear");
        new TrendCommandParser().parse(model, arguments);
    }

    private void assertTrendCommandEquals(TrendCommand expected, TrendCommand actual) {
        assertEquals(expected.category, actual.category);
        assertEquals(expected.measure, actual.measure);
        assertEquals(expected.filters, actual.filters);
    }
}

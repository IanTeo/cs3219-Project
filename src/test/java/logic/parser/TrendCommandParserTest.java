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

public class TrendCommandParserTest {
    @Test
    public void parse_measureOnly_validTrendCommand() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("measure", "paper");

        TrendCommand expected = new TrendCommand(Category.TOTAL, Collections.emptyList(), Measure.PAPER);
        assertEquals(expected, new TrendCommandParser().parse(arguments));
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

        Collection<Filter> expectedFilters = Arrays.asList(new PaperVenueFilter(Collections.singletonList("foo")),
                new PaperTitleFilter(Collections.singletonList("bar")),
                new AuthorFilter(Arrays.asList("baz qux", "qux bax")),
                new YearFilter(new YearRange(2000, 2001)));

        TrendCommand expected = new TrendCommand(Category.VENUE, expectedFilters, Measure.AUTHOR);
        assertEquals(expected, new TrendCommandParser().parse(arguments));
    }

    @Test(expected = ParseException.class)
    public void parse_categoryOnly_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("category", "venue");
        new TrendCommandParser().parse(arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_filterOnly_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("venue", "foo");
        new TrendCommandParser().parse(arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidMeasure_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("measure", "invalidMeasure");
        new TrendCommandParser().parse(arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidCategory_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("category", "invalidCategory");
        new TrendCommandParser().parse(arguments);
    }

    @Test(expected = ParseException.class)
    public void parse_invalidYear_throwsParseException() throws Exception {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("year", "invalidYear");
        new TrendCommandParser().parse(arguments);
    }
}

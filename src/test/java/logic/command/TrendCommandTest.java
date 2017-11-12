package logic.command;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import logic.filter.AuthorFilter;
import logic.filter.Filter;
import logic.filter.PaperTitleFilter;
import logic.filter.PaperVenueFilter;
import logic.filter.YearFilter;
import logic.model.Category;
import logic.model.Measure;
import logic.model.YearRange;
import model.Model;
import util.FileReader;
import util.ModelStub;

public class TrendCommandTest {
    private static final String BASE_URL = "TrendCommandTest/%s";
    
    private Model model = new ModelStub();

    private Category category;
    private List<Filter> filters;
    private Measure measure;

    @Test
    public void execute_noFilter_returnsValidJson() {
        String[] expected = FileReader.readFile(String.format(BASE_URL, "Trend_NoFilter.json")).split("\n");

        category = Category.VENUE;
        measure = Measure.PAPER;
        TrendCommand trendCommand = new TrendCommand(model, measure, category, Collections.emptyList());
        assertEquals(expected[0], trendCommand.execute());

        category = Category.PAPER;
        measure = Measure.AUTHOR;
        trendCommand = new TrendCommand(model, measure,category, Collections.emptyList());
        assertEquals(expected[1], trendCommand.execute());

        category = Category.TOTAL;
        measure = Measure.VENUE;
        trendCommand = new TrendCommand(model, measure, category, Collections.emptyList());
        assertEquals(expected[2], trendCommand.execute());
    }

    @Test
    public void execute_oneFilter_returnsValidJson() {
        String[] expected = FileReader.readFile(String.format(BASE_URL, "Trend_OneFilter.json")).split("\n");

        category = Category.VENUE;
        filters = Collections.singletonList(new AuthorFilter("AuThOR WiTh PaPeRs p2 P3 p4"));
        measure = Measure.INCITATION;
        TrendCommand trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[0], trendCommand.execute());

        category = Category.TOTAL;
        filters = Collections.singletonList(new PaperTitleFilter("VeNuE SiNgApOrE wItH aUtHoR a2 CiTe P2"));
        measure = Measure.OUTCITATION;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[1], trendCommand.execute());

        category = Category.PAPER;
        filters = Collections.singletonList(new PaperVenueFilter("iCsE"));
        measure = Measure.AUTHOR;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[2], trendCommand.execute());

        category = Category.VENUE;
        filters = Collections.singletonList(new YearFilter(new YearRange(2009, 2011)));
        measure = Measure.PAPER;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[3], trendCommand.execute());
    }

    @Test
    public void execute_twoFilters_returnsValidJson() {
        String[] expected = FileReader.readFile(String.format(BASE_URL, "Trend_TwoFilters.json")).split("\n");

        category = Category.TOTAL;
        filters = Arrays.asList(new AuthorFilter("author with papers P2 P3 P4"),
                new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"));
        measure = Measure.VENUE;
        TrendCommand trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[0], trendCommand.execute());

        category = Category.PAPER;
        filters = Arrays.asList(new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"),
                new PaperVenueFilter("icse"));
        measure = Measure.INCITATION;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[1], trendCommand.execute());

        category = Category.VENUE;
        filters = Arrays.asList(new PaperVenueFilter("icse"),
                new YearFilter(new YearRange(2009, 2011)));
        measure = Measure.OUTCITATION;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[2], trendCommand.execute());

        category = Category.TOTAL;
        filters = Arrays.asList(new YearFilter(new YearRange(2009, 2011)),
                new AuthorFilter("author with papers P1 P2"));
        measure = Measure.AUTHOR;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[3], trendCommand.execute());
    }

    @Test
    public void execute_threeFilters_returnsValidJson() {
        String[] expected = FileReader.readFile(String.format(BASE_URL, "Trend_ThreeFilters.json")).split("\n");

        category = Category.PAPER;
        filters = Arrays.asList(new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"),
                new PaperVenueFilter("icse"),
                new YearFilter(new YearRange(2009, 2011)));
        measure = Measure.INCITATION;
        TrendCommand trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[0], trendCommand.execute());

        category = Category.VENUE;
        filters = Arrays.asList(new PaperVenueFilter("icse"),
                new YearFilter(new YearRange(2009, 2011)),
                new AuthorFilter("author with papers P1 P2"));
        measure = Measure.OUTCITATION;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[1], trendCommand.execute());

        category = Category.TOTAL;
        filters = Arrays.asList(new YearFilter(new YearRange(2009, 2011)),
                new AuthorFilter("author with papers P1 P2"),
                new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"));
        measure = Measure.AUTHOR;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[2], trendCommand.execute());

        category = Category.PAPER;
        filters = Arrays.asList(new AuthorFilter("author with papers P2 P3 P4"),
                new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"),
                new PaperVenueFilter("icse"));
        measure = Measure.VENUE;
        trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[3], trendCommand.execute());
    }

    @Test
    public void execute_allFilters_returnsValidJson() {
        String[] expected = FileReader.readFile(String.format(BASE_URL, "Trend_AllFilters.json")).split("\n");

        category = Category.PAPER;
        filters = Arrays.asList(
                new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"),
                new PaperVenueFilter("icse"),
                new YearFilter(new YearRange(2009, 2011)),
                new AuthorFilter("author with papers P1 P2"));
        measure = Measure.INCITATION;
        TrendCommand trendCommand = new TrendCommand(model, measure, category, filters);
        assertEquals(expected[0], trendCommand.execute());
    }
}

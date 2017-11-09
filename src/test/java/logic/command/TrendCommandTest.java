package logic.command;

import static logic.model.Category.TOTAL;
import static logic.model.Category.VENUE;
import static logic.model.Measure.PAPER;
import static org.junit.Assert.assertEquals;

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
    private static final Model MODEL = new ModelStub();

    private Category category;
    private List<Filter> filters;
    private Measure measure;

    @Test
    public void execute_measureOnly_returnsValidJson() {
        measure = Measure.AUTHOR;
        TrendCommand trendCommand = new TrendCommand(TOTAL, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        String expected = FileReader.readFile("Trend_MeasureAuthor.json");
        assertEquals(expected, trendCommand.execute());

        measure = Measure.PAPER;
        trendCommand = new TrendCommand(TOTAL, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasurePaper.json");
        assertEquals(expected, trendCommand.execute());

        measure = Measure.VENUE;
        trendCommand = new TrendCommand(TOTAL, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureVenue.json");
        assertEquals(expected, trendCommand.execute());

        measure = Measure.INCITATION;
        trendCommand = new TrendCommand(TOTAL, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureIncitation.json");
        assertEquals(expected, trendCommand.execute());

        measure = Measure.OUTCITATION;
        trendCommand = new TrendCommand(TOTAL, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureOutcitation.json");
        assertEquals(expected, trendCommand.execute());
    }

    @Test
    public void execute_measureAndCategoryOnly_returnsValidJson() {
        category = Category.VENUE;
        measure = Measure.AUTHOR;
        TrendCommand trendCommand = new TrendCommand(category, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        String expected = FileReader.readFile("Trend_MeasureAuthorCategoryVenue.json");
        assertEquals(expected, trendCommand.execute());

        category = Category.TITLE;
        measure = Measure.AUTHOR;
        trendCommand = new TrendCommand(category, Collections.emptyList(), measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureAuthorCategoryTitle.json");
        assertEquals(expected, trendCommand.execute());
    }

    @Test
    public void execute_allParametersPresent_returnsValidJson() {
        category = VENUE;
        filters = Collections.singletonList(new AuthorFilter(Collections.singletonList("AuThOR WiTh PaPeRs p2 P3 p4")));
        measure = PAPER;
        TrendCommand trendCommand = new TrendCommand(category, filters, measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        String expected = FileReader.readFile("Trend_MeasureAuthorCategoryVenueFilterAuthor.json");
        assertEquals(expected, trendCommand.execute());

        category = VENUE;
        filters = Collections.singletonList(new PaperTitleFilter(Collections.singletonList("VeNuE SiNgApOrE wItH aUtHoR a2 CiTe P2")));
        measure = PAPER;
        trendCommand = new TrendCommand(category, filters, measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureAuthorCategoryVenueFilterTitle.json");
        assertEquals(expected, trendCommand.execute());

        category = VENUE;
        filters = Collections.singletonList(new PaperVenueFilter(Collections.singletonList("iCsE")));
        measure = PAPER;
        trendCommand = new TrendCommand(category, filters, measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureAuthorCategoryVenueFilterVenue.json");
        assertEquals(expected, trendCommand.execute());

        category = VENUE;
        filters = Collections.singletonList(new YearFilter(new YearRange(2009, 2011)));
        measure = PAPER;
        trendCommand = new TrendCommand(category, filters, measure);
        trendCommand.setParameters(MODEL, Collections.emptyMap());
        expected = FileReader.readFile("Trend_MeasureAuthorCategoryVenueFilterYear.json");
        assertEquals(expected, trendCommand.execute());
    }
}

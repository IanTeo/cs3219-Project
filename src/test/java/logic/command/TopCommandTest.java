package logic.command;

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
import org.junit.Before;
import org.junit.Test;
import util.FileReader;
import util.ModelStub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TopCommandTest {
    private static final String BASE_URL = "TopCommandTest/%s";
    
    private Model model = new ModelStub();

    @Test
    public void execute_validCategoryPaperAndMeasureIncitation_printJson() throws Exception {
        int count = 100;
        Category category = Category.PAPER;
        Measure measure = Measure.INCITATION;
        List<Filter> filters = new ArrayList<>();
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidPaperIncitationResult.json"));

        filters.add(new PaperVenueFilter("Icse"));
        filters.add(new AuthorFilter("author WiTh papers P1 P3"));
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidPaperIncitationFilterResult.json"));
    }

    @Test
    public void execute_validCategoryVenueAndMeasurePaper_printJson() throws Exception {
        int count = 2;
        Category category = Category.VENUE;
        Measure measure = Measure.PAPER;
        List<Filter> filters = new ArrayList<>();
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidVenuePaperResult.json"));

        filters.add(new YearFilter(new YearRange(2012, 2015)));
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidVenuePaperFilterResult.json"));

        measure = Measure.AUTHOR;
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidVenueAuthorFilterResult.json"));
    }

    @Test
    public void execute_validCategoryAuthorAndMeasureOutcitation_printJson() throws Exception {
        int count = 2;
        Category category = Category.AUTHOR;
        Measure measure = Measure.OUTCITATION;
        List<Filter> filters = new ArrayList<>();
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidAuthorOutcitationResult.json"));

        filters.add(new AuthorFilter("author with papers P1 P2", "author with papers P1 P3"));
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidAuthorOutcitationFilterResult.json"));

        measure = Measure.VENUE;
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidAuthorVenueFilterResult.json"));

        filters.add(new PaperTitleFilter("venue ICSE with authors A1 A2 cite P1 P3"));
        assertCommand(count, measure, category, filters, String.format(BASE_URL, "Top_ValidAuthorVenueFilterPaperResult.json"));
    }

    private void assertCommand(int count, Measure measure, Category category,
            List<Filter> filters, String expectedOutputFileName) throws Exception {
        TopCommand command = new TopCommand(model, count, measure, category, filters);

        String actual = command.execute();
        String expected = FileReader.readFile(expectedOutputFileName);
        assertEquals(expected, actual);
    }
}

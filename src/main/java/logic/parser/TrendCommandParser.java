package logic.parser;

import static logic.model.Category.TOTAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import logic.command.TrendCommand;
import logic.exception.ParseException;
import logic.filter.AuthorFilter;
import logic.filter.Filter;
import logic.filter.PaperTitleFilter;
import logic.filter.PaperVenueFilter;
import logic.model.Measure;
import logic.model.Category;
import logic.filter.YearFilter;
import logic.model.YearRange;

public class TrendCommandParser {
    private static final String HELP = "Error: %s%nUsage: trend [search term] [search values] NUM [ordering] " +
            "FROM [start year]-[end year]%n" +
            "E.g. Author Joshua, Ian NUM Paper FROM 2000-2012%n" +
            "Venue ICSE, IAI NUM Paper FROM 2000%n" +
            "This command returns a JSON file representing the trend of the search over the period of \"years\"" +
            "for the corresponding \"searchKeyword\"";

    public TrendCommand parse(Map<String, String> arguments) throws ParseException {
        Category category = getCategory(arguments);
        List<Filter> filters = getFilters(arguments);
        try {
            Measure measure = Measure.valueOf(arguments.get("measure").toUpperCase());
            return new TrendCommand(category, filters, measure);
        } catch (Exception e) {
            throw new ParseException("Invalid measure.");
        }
    }

    private Category getCategory(Map<String, String> arguments) throws ParseException {
        if (!arguments.containsKey("category")) {
            return TOTAL;
        }

        try {
            return Category.valueOf(arguments.get("category").toUpperCase());
        } catch (Exception e) {
            throw new ParseException("Invalid category");
        }
    }

    private List<Filter> getFilters(Map<String, String> arguments) throws ParseException {
        List<Filter> filters = new ArrayList<>();
        if (arguments.containsKey("venue")) {
            filters.add(new PaperVenueFilter(arguments.get("venue")));
        }
        if (arguments.containsKey("paper")) {
            filters.add(new PaperTitleFilter(arguments.get("paper")));
        }
        if (arguments.containsKey("author")) {
            filters.add(new AuthorFilter(arguments.get("author").split(",")));
        }
        if (arguments.containsKey("year")) {
            filters.add(new YearFilter(getYearRange(arguments.get("year"))));
        }

        return filters;
    }

    private YearRange getYearRange(String year) throws ParseException {
        try {
            return new YearRange(year);
        } catch (Exception e) {
            throw new ParseException("Invalid year");
        }
    }
}

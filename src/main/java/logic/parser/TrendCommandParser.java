package logic.parser;

import static logic.model.QueryKeyword.TOTAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import logic.command.TrendCommand;
import logic.exception.ParseException;
import logic.model.AuthorFilter;
import logic.model.Filter;
import logic.model.PaperTitleFilter;
import logic.model.PaperVenueFilter;
import logic.model.QueryKeyword;
import logic.model.YearFilter;
import logic.model.YearRange;

public class TrendCommandParser {
    private static final String HELP = "Error: %s\nUsage: trend [search term] [search values] NUM [ordering] " +
            "FROM [start year]-[end year]\n" +
            "E.g. Author Joshua, Ian NUM Paper FROM 2000-2012\n" +
            "Venue ICSE, IAI NUM Paper FROM 2000\n" +
            "This command returns a JSON file representing the trend of the search over the period of \"years\"" +
            "for the corresponding \"searchKeyword\"";

    public TrendCommand parse(Map<String, String> arguments) throws ParseException {
        QueryKeyword category = getCategory(arguments);
        List<Filter> filters = getFilters(arguments);
        QueryKeyword measure = getQueryKeyword(arguments.get("measure"));

        return new TrendCommand(category, filters, measure);
    }

    private QueryKeyword getCategory(Map<String, String> arguments) {
        if (!arguments.containsKey("category")) {
            return TOTAL;
        }

        return getQueryKeyword(arguments.get("category"));
    }

    private List<Filter> getFilters(Map<String, String> arguments) throws ParseException {
        List<Filter> filters = new ArrayList<>();
        if (arguments.containsKey("venue")) {
            filters.add(new PaperVenueFilter(getValues(arguments.get("venue"))));
        }
        if (arguments.containsKey("paper")) {
            filters.add(new PaperTitleFilter(getValues(arguments.get("title"))));
        }
        if (arguments.containsKey("author")) {
            filters.add(new AuthorFilter(getValues(arguments.get("author"))));
        }
        if (arguments.containsKey("year")) {
            filters.add(new YearFilter(getYearRange(arguments.get("year"))));
        }

        return filters;
    }

    /**
     * Parses {@code queryKeyword} into a {@code QueryKeyword}.
     */
    private QueryKeyword getQueryKeyword(String queryKeyword) {
        return QueryKeyword.valueOf(queryKeyword.toUpperCase());
    }

    private List<String> getValues(String values) {
        return Arrays.stream(values.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private YearRange getYearRange(String year) throws ParseException {
        List<Integer> years;
        try {
            years = Arrays.stream(year.split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException nfe) {
            throw new ParseException("Invalid year value.");
        }

        if (years.size() == 1) {
            return new YearRange(years.get(0));
        } else {
            return new YearRange(years.get(0), years.get(1));
        }
    }
}

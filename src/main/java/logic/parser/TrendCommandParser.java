package logic.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import logic.command.Command;
import logic.command.TrendCommand;
import logic.exception.ParseException;
import logic.model.QueryKeyword;
import logic.model.Search;
import logic.model.YearRange;

public class TrendCommandParser implements CommandParser {
    private static final Pattern PATTERN = Pattern.compile("(?<searchCategory>[a-zA-Z]+) (?<searchKeyword>.+) " +
            "NUM (?<ordering>[a-zA-Z]+)( FROM (?<yearRange>[0-9-]+))?");
    private static final String HELP = "Error: %s\nUsage: trend [search term] [search values] NUM [ordering] " +
            "FROM [start year]-[end year]\n" +
            "E.g. Author Joshua, Ian NUM Paper FROM 2000-2012\n" +
            "Venue ICSE, IAI NUM Paper FROM 2000\n" +
            "This command returns a JSON file representing the trend of the search over the period of \"years\"" +
            "for the corresponding \"searchKeyword\"";

    @Override
    public TrendCommand parse(Map<String, String> argumentMap) throws ParseException {
        // lousy patch to solve merge conflicts (For now)
        String arguments = String.format("%s %s NUM %s FROM %s",
                argumentMap.get("searchTerm"), argumentMap.get("searchValues"),
                argumentMap.get("ordering"), argumentMap.get("yearRange"));
        Matcher matcher = PATTERN.matcher(arguments);
        if (!matcher.matches()) {
            throw new ParseException(String.format(HELP, "Invalid argument"));
        }

        Search toSearch = getSearchTerms(matcher.group("searchCategory"), matcher.group("searchKeyword"));
        QueryKeyword ordering = getOrdering(matcher.group("ordering"));
        YearRange yearRange = getYearRange(matcher.group("yearRange"));

        return new TrendCommand(toSearch, ordering, yearRange);
    }

    private Search getSearchTerms(String key, String value) throws ParseException {
        QueryKeyword searchKey = getQueryKeyword(key);
        if (!Command.isValidSearchCategory(searchKey)) {
            throw new ParseException(String.format(HELP, "Invalid search key"));
        }

        List<String> searchValues = getValues(value);
        return new Search(searchKey, searchValues);
    }

    private QueryKeyword getOrdering(String ordering) throws ParseException {
        QueryKeyword key = getQueryKeyword(ordering);

        if (!Command.isValidOrdering(key)) {
            throw new ParseException(String.format(HELP, "Invalid ordering"));
        }

        return key;
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

    private YearRange getYearRange(String yearRange) throws ParseException {
        List<Integer> years = Arrays.stream(yearRange.split("-")).map(Integer::getInteger).collect(Collectors.toList());
        if (years.contains(null) || years.size() > 2) {
            throw new ParseException("Invalid year range");
        }

        if (years.size() == 0) {
            return new YearRange();
        } else if (years.size() == 1) {
            return new YearRange(years.get(0));
        } else {
            return new YearRange(years.get(0), years.get(1));
        }
    }
}

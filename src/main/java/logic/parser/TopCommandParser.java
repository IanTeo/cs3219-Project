package logic.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logic.command.TopCommand;
import logic.exception.ParseException;
import logic.filter.AuthorFilter;
import logic.filter.Filter;
import logic.filter.PaperTitleFilter;
import logic.filter.PaperVenueFilter;
import logic.model.Measure;
import logic.model.Category;
import logic.filter.YearFilter;
import logic.model.YearRange;
import model.Model;

public class TopCommandParser {
    public static final String ERROR_MISSING_PARAMETERS = "Missing parameters:%nRequired parameters: count, measure, category";
    
    public TopCommand parse(Model model, Map<String, String> arguments) throws ParseException {
        if (!containExpectedArguments(arguments)) {
            throw new ParseException(ERROR_MISSING_PARAMETERS);
        }
        
        int count = getCount(arguments.get("count"));
        Measure measure = getMeasure(arguments.get("measure"));
        Category category = getCategory(arguments.get("category"));
        List<Filter> filters = getFilters(arguments);
        return new TopCommand(model, count, measure, category, filters);
    }
    
    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("count")
                && paramMap.containsKey("measure")
                && paramMap.containsKey("category");
    }

    private int getCount(String count) throws ParseException {
        try {
            return Integer.parseInt(count);
        } catch (Exception e) {
            throw new ParseException("Invalid Measure");
        }
    }
    
    private Measure getMeasure(String measure) throws ParseException {
        try {
            return Measure.valueOf(measure.toUpperCase());
        } catch (Exception e) {
            throw new ParseException("Invalid Measure");
        }
    }
    
    private Category getCategory(String category) throws ParseException {
        try {
            return Category.valueOf(category.toUpperCase());
        } catch (Exception e) {
            throw new ParseException("Invalid category");
        }
    }

    private List<Filter> getFilters(Map<String, String> arguments) throws ParseException {
        List<Filter> filters = new ArrayList<>();
        if (arguments.containsKey("venue")) {
            filters.add(new PaperVenueFilter(arguments.get("venue").split(",")));
        }
        if (arguments.containsKey("paper")) {
            filters.add(new PaperTitleFilter(arguments.get("paper").split(",")));
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

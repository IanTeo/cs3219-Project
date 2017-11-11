package logic.command;

import java.util.*;
import java.util.stream.Collectors;

import logic.filter.*;
import logic.jsonconverter.JsonConverter;
import logic.model.Category;
import logic.model.Measure;
import logic.model.YearRange;
import logic.utility.CollectionUtility;
import logic.utility.MapUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import logic.exception.ParseException;
import model.Author;
import model.Model;
import model.Paper;
import util.StringUtil;

public class TopCommand implements Command{
    public static final String COMMAND_WORD = "top";
    public static final String HELP = "Error: %s%n" + COMMAND_WORD + "%n" +
            "This command returns a JSON file representing the top # of authors/papers for the specified venue%n" +
            "Required fields: count, category, measure%n" +
            "Optional fields: venue, paper, author, year%n" +
            "Example: count=5&category=paper&measure=incitation";
    private Model model;
    private int count;
    private Category category;
    private Measure measure;
    private List<Filter> filters;

    public String execute() {
        Collection<Paper> papers = removeUnwantedValues(model.getPapers());
        Map<String, Collection<Paper>> categoryToPaper = MapUtility.groupPaper(papers, category);
        if (category == Category.VENUE) {
            categoryToPaper = MapUtility.mergeEqualKeys(categoryToPaper,
                    Filter.getFilterOfCategory(category, filters), category);
        }
        Map<String, Integer> categoryToMeasure = MapUtility.sumMap(categoryToPaper, measure);
        List<Map.Entry<String, Integer>> entryList = categoryToMeasure.entrySet().stream()
                .sorted((e1, e2) -> {
                    if (e2.getValue().equals(e1.getValue()))
                        return e1.getKey().compareTo(e2.getKey()); // Ascending by group
                    return e2.getValue().compareTo(e1.getValue()); // Descending by count
                })
                .collect(Collectors.toList());

        if (entryList.size() > count) {
            entryList = entryList.subList(0, count);
        }

        return JsonConverter.entryListToJson(entryList, category.toString().toLowerCase(),
                measure.toString().toLowerCase()).toString();
    }

    public void setParameters(Model model, Map<String, String> paramMap) throws ParseException {
        if (!containExpectedArguments(paramMap)) {
            throw new ParseException(String.format(HELP, "Error parsing parameters"));
        }
        this.model = model;
        this.count = Integer.parseInt(paramMap.get("count"));
        this.category = Category.valueOf(paramMap.get("category").toUpperCase());
        this.measure = Measure.valueOf(paramMap.get("measure").toUpperCase());
        this.filters = new ArrayList<>();

        if (paramMap.containsKey("venue")) {
            filters.add(new PaperVenueFilter(paramMap.get("venue").split(",")));
        }

        if (paramMap.containsKey("author")) {
            filters.add(new AuthorFilter(paramMap.get("author").split(",")));
        }

        if (paramMap.containsKey("paper")) {
            filters.add(new PaperTitleFilter(paramMap.get("paper").split(",")));
        }

        if (paramMap.containsKey("year")) {
            filters.add(new YearFilter(new YearRange(paramMap.get("year"))));
        }
    }

    private boolean containExpectedArguments(Map<String, String> paramMap) {
        return paramMap.containsKey("count")
                && paramMap.get("count").matches("[0-9]+")
                && paramMap.containsKey("category")
                && paramMap.containsKey("measure");
    }

    private Collection<Paper> removeUnwantedValues(Collection<Paper> papers) {
        for (Filter filter : filters) {
            papers = filter.filter(papers);
        }

        if (category == Category.VENUE) {
            papers = CollectionUtility.removeFromCollection(papers,
                    paper -> !paper.getVenue().equals("<venue unspecified>"));
        }
        return papers;
    }
}

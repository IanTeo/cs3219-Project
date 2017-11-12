package logic.command;

import java.util.*;
import java.util.stream.Collectors;

import logic.filter.*;
import logic.jsonconverter.JsonConverter;
import logic.model.Category;
import logic.model.Measure;
import logic.utility.CollectionUtility;
import logic.utility.MapUtility;

import model.Model;
import model.Paper;

public class TopCommand implements Command{
    public static final String COMMAND_WORD = "top";
    
    private Model model;
    public final int count;
    public final Measure measure;
    public final Category category;
    public final List<Filter> filters;

    public TopCommand(Model model, int count, Measure measure, Category category, List<Filter> filters) {
        this.model = model;
        this.count = count;
        this.measure = measure;
        this.category = category;
        this.filters = filters;
    }
    public String execute() {
        Collection<Paper> papers = removeUnwantedValues(model.getPapers());
        Map<String, Collection<Paper>> categoryToPaper = MapUtility.groupPaper(papers, category);
        if (!filters.isEmpty()) {
            categoryToPaper = MapUtility.mergeEqualKeys(categoryToPaper,
                    Filter.getFilterOfCategory(category, filters));
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

        return JsonConverter.entryListToJson(entryList).toString();
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

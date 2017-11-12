package logic.command;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import logic.jsonconverter.JsonConverter;
import logic.filter.Filter;
import logic.model.Measure;
import logic.model.Category;
import logic.filter.YearFilter;
import logic.model.YearRange;
import logic.utility.CollectionUtility;
import logic.utility.MapUtility;
import model.Model;
import model.Paper;

public class TrendCommand implements Command {
    public static final String COMMAND_WORD = "trend";
    
    private Model model;
    public final Measure measure;
    public final Category category;
    public final Collection<Filter> filters;

    public TrendCommand(Model model, Measure measure, Category category, Collection<Filter> filters) {
        this.model = model;
        this.measure = measure;
        this.category = category;
        this.filters = filters;
    }

    public String execute() {
        Collection<Paper> papers = removeUnwantedValues(model.getPapers());
        Map<String, Collection<Paper>> queryToPaper = MapUtility.groupPaper(papers, category);
        if (!filters.isEmpty()) {
            queryToPaper = MapUtility.mergeEqualKeys(queryToPaper,
                    Filter.getFilterOfCategory(category, filters));
        }
        Map<String, Map<Integer, Collection<Paper>>> queryToYearToPaper = MapUtility.groupPaperByYear(queryToPaper);
        Map<String, Map<Integer, Integer>> queryToYearToCount = MapUtility.sumMaps(queryToYearToPaper, measure);
        populateEmptyYears(queryToYearToCount);
        return JsonConverter.mapsToJson(queryToYearToCount).toJSONString();
    }

    private Collection<Paper> removeUnwantedValues(Collection<Paper> papers) {
        for (Filter filter : filters) {
            papers = filter.filter(papers);
        }
        papers = CollectionUtility.removeFromCollection(papers, paper -> paper.getYear() != 0);
        return papers;
    }

    private void populateEmptyYears(Map<String, Map<Integer, Integer>> map) {
        YearFilter yearFilter = getYearFilter();
        getYearRange(map, yearFilter)
                .ifPresent(range -> range.stream().forEach(year -> map.values() // for each year
                        .forEach(innerMap -> innerMap.putIfAbsent(year, 0)))); // put into Map<Integer, Integer> if year is missing
    }

    private Optional<YearRange> getYearRange(Map<String, Map<Integer, Integer>> map, YearFilter yearFilter) {
        if (yearFilter != null) {
            return getYearRange(yearFilter.getValuesToFilter().stream()
                    .map(Integer::parseInt) // stream of years to filter
                    .collect(Collectors.toList()));
        } else { // get minimum & maximum year across all years in map
            return getYearRange(map.values().stream()
                    .map(Map::keySet)
                    .flatMap(Set::stream) // stream of all year values
                    .collect(Collectors.toList()));
        }
    }

    private Optional<YearRange> getYearRange(Collection<Integer> integers) {
        int min = integers.stream().min(Integer::compareTo).orElse(-1); // -1 implies integers is empty
        int max = integers.stream().max(Integer::compareTo).orElse(-1);
        return (min == -1 || max == -1) ? Optional.empty() : Optional.of(new YearRange(min, max));
    }

    private YearFilter getYearFilter() {
        return filters.stream()
                    .filter(filter -> filter instanceof YearFilter)
                    .map(filter -> (YearFilter) filter)
                    .findFirst().orElse(null);
    }
}

package logic.command;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import logic.jsonconverter.TrendToJsonConverter;
import logic.model.Filter;
import logic.model.PaperTitleFilter;
import logic.model.PaperVenueFilter;
import logic.model.QueryKeyword;
import logic.model.YearFilter;
import logic.model.YearRange;
import logic.utility.MapUtility;
import model.Model;
import model.Paper;

public class TrendCommand implements Command {
    public static final String COMMAND_WORD = "trend";
    private Model model;

    private final TrendToJsonConverter jsonConverter = new TrendToJsonConverter();
    private final QueryKeyword category;
    private final Collection<Filter> filters; // terms to filter by. e.g. {Venue, [x, y]}
    private final QueryKeyword measure;

    public TrendCommand(QueryKeyword category, Collection<Filter> filters, QueryKeyword measure) {
        this.category = category;
        this.filters = filters;
        this.measure = measure;
    }

    public String execute() {
        Collection<Paper> papers = removeUnwantedValues(model.getPapers());
        Map<String, Collection<Paper>> queryToPaper = MapUtility.groupPaper(papers, category);
        queryToPaper = MapUtility.mergeEqualKeys(queryToPaper, getFilterOfCategory(), category);
        Map<String, Map<Integer, Collection<Paper>>> queryToYearToPaper = MapUtility.groupPaper(queryToPaper);
        Map<String, Map<Integer, Integer>> queryToYearToCount = MapUtility.sumMaps(queryToYearToPaper, measure);
        populateEmptyYears(queryToYearToCount);

        return jsonConverter.mapsToJson(queryToYearToCount).toJSONString();
    }

    private Filter getFilterOfCategory() {
        switch (category) {
            case TITLE:
                return filters.stream()
                        .filter(filter -> filter instanceof PaperTitleFilter)
                        .findFirst()
                        .orElse(null);
            case TOTAL:
                return null;
            case VENUE:
                return filters.stream()
                        .filter(filter -> filter instanceof PaperVenueFilter)
                        .findFirst()
                        .orElse(null);
            default:
                throw new AssertionError("Not yet implemented.");
        }
    }

    private Collection<Paper> removeUnwantedValues(Collection<Paper> papers) {
        filters.forEach(filter -> filter.filter(papers));
        return papers;
    }

    private void populateEmptyYears(Map<String, Map<Integer, Integer>> map) {
        // get minimum & maximum year across all keys in map
        Optional<YearRange> yearRange = getYearRange(map.values().stream() // Stream<<Map<Integer, Integer>>
                .map(Map::keySet) // Stream<Set<Integer>>
                .flatMap(Set::stream) // Stream<Integer>
                .collect(Collectors.toList())); // Collection<Integer>

        yearRange.ifPresent(range -> range.stream()
                .forEach(year -> map.values() // for each year
                        .forEach(innerMap -> innerMap.putIfAbsent(year, 0)))); // put into innerMap if year is missing
    }

    private Optional<YearRange> getYearRange(Collection<Integer> integers) {
        Optional<YearFilter> yearFilter = filters.stream()
                .filter(filter -> filter instanceof YearFilter)
                .map(filter -> (YearFilter) filter)
                .findFirst();

        if (yearFilter.isPresent()) {
            Collection<Integer> years = yearFilter.get().getValuesToFilter().stream()
                    .map(Integer::parseInt).collect(Collectors.toList());
            int min = years.stream().min(Integer::compareTo).orElse(-1); // if integers is empty, return -1
            int max = years.stream().max(Integer::compareTo).orElse(-1);
            return Optional.of(new YearRange(min, max));
        } else {
            int min = integers.stream().min(Integer::compareTo).orElse(-1);
            int max = integers.stream().max(Integer::compareTo).orElse(-1);
            YearRange yearRange = new YearRange(min, max);
            return (min == -1 || max == -1) ? Optional.empty() : Optional.of(yearRange);
        }
    }

    public void setParameters(Model model, Map<String, String> paramMap) {
        this.model = model;
    }
}

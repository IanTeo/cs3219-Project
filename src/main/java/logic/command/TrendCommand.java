package logic.command;

import static logic.model.MapEnum.PAPER_YEAR;
import static logic.model.QueryKeyword.AUTHOR;
import static logic.model.QueryKeyword.TITLE;
import static logic.model.QueryKeyword.VENUE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import logic.Precondition;
import logic.model.PaperTitlePredicate;
import logic.model.PaperVenuePredicate;
import logic.model.QueryKeyword;
import logic.model.Search;
import logic.model.YearPredicate;
import logic.model.YearRange;
import logic.utility.CollectionUtility;
import logic.utility.Remapper;
import model.Author;
import model.Model;
import model.Paper;
import util.StringUtil;

public class TrendCommand implements Command {
    public static final String COMMAND_WORD = "trend";
    private Model model;

    private final TrendToJsonConverter jsonConverter = new TrendToJsonConverter();
    private final Search search;
    private final QueryKeyword ordering;
    private final YearRange yearRange;

    public TrendCommand(Search search, QueryKeyword ordering, YearRange yearRange) {
        Precondition.checkArgument(Command.isValidSearchCategory(search.getSearchCategory()));
        Precondition.checkArgument(Command.isValidOrdering(ordering));
        this.search = search;
        this.ordering = ordering;
        this.yearRange = yearRange;
    }

    public String execute() {
        Map<String, Map<Integer, Integer>> result;

        switch (search.getSearchCategory()) {
            case AUTHOR:
                result = processAuthor();
                break;
            case VENUE:
                result = processVenue();
                break;
            case TITLE:
                result = processTitle();
                break;
            default:
                throw new AssertionError("Not yet implemented.");
        }

        return jsonConverter.toJson(result).toJSONString();
    }

    private Map<String, Map<Integer, Integer>> processAuthor() {
        Map<String, Collection<Paper>> authorToPaper = search.getSearchKeywords().stream().map(model::getAuthor)
                .collect(Collectors.toMap(Author::getName, Author::getPapers));

        removeUnwantedValues(authorToPaper, AUTHOR);
        Map<String, Map<Integer, Integer>> authorToYearToCount = Remapper.sum(Remapper.groupPaper(authorToPaper, PAPER_YEAR), ordering);
        populateEmptyYears(authorToYearToCount);
        return authorToYearToCount;
    }

    private Map<String, Map<Integer, Integer>> processVenue() {
        Map<String, Collection<Paper>> venueToPaper = model.getPapers().stream()
                .collect(Collectors.groupingBy(Paper::getVenue, Collectors.toCollection(ArrayList::new)));

        removeUnwantedValues(venueToPaper, VENUE);
        venueToPaper = mergeEqualKeys(venueToPaper);
        Map<String, Map<Integer, Integer>> venueToYearToCount = Remapper.sum(Remapper.groupPaper(venueToPaper, PAPER_YEAR), ordering);
        populateEmptyYears(venueToYearToCount);
        return venueToYearToCount;
    }

    private Map<String, Map<Integer, Integer>> processTitle() {
        Map<String, Collection<Paper>> titleToPaper = model.getPapers().stream()
                .collect(Collectors.groupingBy(Paper::getTitle, Collectors.toCollection(ArrayList::new)));

        removeUnwantedValues(titleToPaper, TITLE);
        titleToPaper = mergeEqualKeys(titleToPaper);
        Map<String, Map<Integer, Integer>> titleToYearToCount = Remapper.sum(Remapper.groupPaper(titleToPaper, PAPER_YEAR), ordering);
        populateEmptyYears(titleToYearToCount);
        return titleToYearToCount;
    }

    private void removeUnwantedValues(Map<String, Collection<Paper>> map, QueryKeyword filter) {
        Predicate<String> predicate;
        switch (filter) {
            case TITLE:
                predicate = new PaperTitlePredicate(search.getSearchKeywords());
                break;
            case VENUE:
                predicate = new PaperVenuePredicate(search.getSearchKeywords());
                break;
            case AUTHOR:
                predicate = str -> true;
                break;
            default:
                throw new AssertionError("Not yet implemented.");
        }
        CollectionUtility.removeFromCollection(map.keySet(), predicate);
        CollectionUtility.removeFromCollections(map.values(), new YearPredicate(yearRange));
    }

    /**
     * Merges keys in {@code map} that are considered as equal. E.g. {@code venue == "ICSE"} and {@code venue == "ICSE@ICSE"}
     * are considered as equal, however they are mapped into different keys.
     * Ian, this method is necessary because we do not have Venue classes etc. If we have done something like
     * Map<Venue, Collection<Paper>> or Map<Title, Collection<Paper>>, then there's no need for this method because
     * they would have already been merged.
     * I'm of the opinion to have a static boolean variable isCaseSensitive in Venue class or something like that, which
     * causes the equals method to work differently depending on the value.
     */
    private Map<String, Collection<Paper>> mergeEqualKeys(Map<String, Collection<Paper>> map) {
        Map<String, Collection<Paper>> mergedMap = new HashMap<>();
        for (String searchKeyword : search.getSearchKeywords()) {
            Predicate<String> predicate = getPredicate(searchKeyword);
            map.keySet().stream()
                    .filter(predicate)
                    .forEach(key -> mergedMap.merge(searchKeyword, map.getOrDefault(key, Collections.emptyList()),
                            CollectionUtility.mergeLists()));
        }

        return mergedMap;
    }

    private Predicate<String> getPredicate(String searchKeyword) {
        switch (search.getSearchCategory()) {
            case AUTHOR:
            case TITLE:
                return key -> StringUtil.containsIgnoreCase(key, searchKeyword);
            case VENUE:
                return key -> StringUtil.containsIgnoreCaseVenue(key, searchKeyword);
            default:
                throw new AssertionError("Not yet implemented");
        }
    }

    private void populateEmptyYears(Map<String, Map<Integer, Integer>> map) {
        map.values().forEach(toPopulate -> yearRange.stream().forEach(value -> toPopulate.putIfAbsent(value, 0)));
    }

    public void setParameters(Model model, String arguments) {
        this.model = model;
    }

    private class TrendToJsonConverter {
        public JSONArray toJson(Map<String, Map<Integer, Integer>> toConvert) {
            JSONArray resultArray = new JSONArray();
            for (Map.Entry<String, Map<Integer, Integer>> entry : toConvert.entrySet()) {
                resultArray.add(getResultElement(entry));
            }
            return resultArray;
        }

        private JSONObject getResultElement(Map.Entry<String, Map<Integer, Integer>> entry) {
            JSONObject resultElement = setSearchTerm(entry.getKey());
            JSONArray yearCountArray = getYearCountPairs(entry.getValue());
            resultElement.put("year-count-pair", yearCountArray);
            return resultElement;
        }

        private JSONObject setSearchTerm(String value) {
            JSONObject obj = new JSONObject();
            obj.put(search.getSearchCategory().toString().toLowerCase(), value);
            return obj;
        }

        private JSONArray getYearCountPairs(Map<Integer, Integer> yearCountMap) {
            JSONArray yearCountArray = new JSONArray();
            for (Map.Entry<Integer, Integer> innerEntry : yearCountMap.entrySet()) {
                JSONObject yearCountPair = new JSONObject();
                yearCountPair.put("year", innerEntry.getKey());
                yearCountPair.put("count", innerEntry.getValue());
                yearCountArray.add(yearCountPair);
            }
            return yearCountArray;
        }
    }
}

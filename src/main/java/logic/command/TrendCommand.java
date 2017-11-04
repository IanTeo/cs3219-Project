package logic.command;

import static logic.model.MapEnum.PAPER_YEAR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import logic.Precondition;
import logic.model.QueryKeyword;
import logic.model.YearPredicate;
import logic.model.YearRange;
import logic.utility.CollectionUtility;
import logic.utility.Remapper;
import model.Author;
import model.Model;
import model.Paper;
import util.StringUtil;

public class TrendCommand {
    public static final String COMMAND_WORD = "trend";
    private Model model;

    private final TrendToJsonConverter jsonConverter = new TrendToJsonConverter();
    private final QueryKeyword search;
    private final QueryKeyword ordering;
    private final YearRange yearRange;

    public TrendCommand(QueryKeyword search, QueryKeyword ordering, YearRange yearRange) {
        Precondition.checkArgument(Command.isValidSearchCategory(search));
        Precondition.checkArgument(Command.isValidOrdering(ordering));
        this.search = search;
        this.ordering = ordering;
        this.yearRange = yearRange;
    }

    public String execute() {
        Map<String, Map<Integer, Integer>> result;

        switch (search) {
            case AUTHOR:
                result = processAuthor();
                break;
            case VENUE:
                result = processVenue();
                break;
            default:
                throw new AssertionError("Not yet implemented.");
        }

        return jsonConverter.toJson(result).toJSONString();
    }

    private Map<String, Map<Integer, Integer>> processAuthor() {
        Map<String, Collection<Paper>> authorToPaper = model.getAuthors().stream()
                .collect(Collectors.toMap(Author::getName, Author::getPapers));

        Map<String, Map<Integer, Integer>> authorToYearToCount = Remapper.sum(Remapper.groupPaper(authorToPaper, PAPER_YEAR), ordering);
        removeUnwantedValues(authorToYearToCount);
        populateEmptyYears(authorToYearToCount);
        return authorToYearToCount;
    }

    private Map<String, Map<Integer, Integer>> processVenue() {
        Map<String, Collection<Paper>> venueToPaper = model.getPapers().stream()
                .collect(Collectors.groupingBy(Paper::getVenue, Collectors.toCollection(ArrayList::new)));

        venueToPaper = mergeEqualKeys(venueToPaper);
        Map<String, Map<Integer, Integer>> venueToYearToCount = Remapper.sum(Remapper.groupPaper(venueToPaper, PAPER_YEAR), ordering);
        removeUnwantedValues(venueToYearToCount);
        populateEmptyYears(venueToYearToCount);
        return venueToYearToCount;
    }

    private Map<String, Collection<Paper>> mergeEqualKeys(Map<String, Collection<Paper>> map) {
        Collection<String> usedKeys = new ArrayList<>();
        Map<String, Collection<Paper>> mergedMap = new HashMap<>();
        for (String key1 : map.keySet()) {
            for (String key2 : map.keySet()) {
                if (usedKeys.contains(key1) || key1.equals(key2)) {
                    continue;
                }

                Predicate<String> predicate = getPredicate(key2);
                if (predicate.test(key1)) {
                    Collection<Paper> mergedPapers = CollectionUtility.mergeLists().apply(map.get(key1), map.get(key2));
                    mergedMap.put(key1, mergedPapers);
                    usedKeys.addAll(Arrays.asList(key1, key2));
                }
            }
        }

        return mergedMap;
    }

    private Predicate<String> getPredicate(String searchKeyword) {
        switch (search) {
            case AUTHOR:
                return key -> StringUtil.containsIgnoreCase(key, searchKeyword);
            case VENUE:
                return key -> StringUtil.containsIgnoreCaseVenue(key, searchKeyword);
            default:
                throw new AssertionError("Not yet implemented");
        }
    }

    private void removeUnwantedValues(Map<String, Map<Integer, Integer>> map) {
        // Can add more filtering here
        for (Map<Integer, Integer> intMap : map.values()) {
            CollectionUtility.removeFromCollection(intMap.keySet(), new YearPredicate(yearRange));
        }
    }

    private void populateEmptyYears(Map<String, Map<Integer, Integer>> map) {
        map.values().forEach(toPopulate -> yearRange.stream().forEach(value -> toPopulate.putIfAbsent(value, 0)));
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
            obj.put(search.toString().toLowerCase(), value);
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

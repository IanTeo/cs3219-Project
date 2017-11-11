package logic.utility;

import static logic.model.Category.TOTAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import logic.filter.Filter;
import logic.model.Measure;
import logic.model.Category;
import model.Author;
import model.Paper;
import util.StringUtil;

/**
 * Utility class providing methods to map {@code Collection} and remap {@code Map}.
 */
public class MapUtility {
    /**
     * Groups the {@code Collection<Paper>} in {@code map} according to years.
     */
    public static <T> Map<T, Map<Integer, Collection<Paper>>> groupPaperByYear(Map<T, Collection<Paper>> map) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // map to itself i.e. no change
                entry -> entry.getValue().stream().collect(Collectors.groupingBy(Paper::getYear, // group by years
                        Collectors.toCollection(ArrayList::new)))));
    }

    /**
     * Groups {@code papers} according to {@code groupBy}.
     * E.g. If {@code groupBy == VENUE}, groups papers according to their venue names.
     */
    public static Map<String, Collection<Paper>> groupPaper(Collection<Paper> papers, Category groupBy) {
        HashMap<String, Collection<Paper>> groupMap = new HashMap<>();
        for (Paper paper : papers) {
            for (String group : getGroupsFromPaper(groupBy, paper)) {
                if (groupMap.containsKey(group)) {
                    groupMap.get(group).add(paper);
                } else {
                    Collection<Paper> paperCollection = new ArrayList<>();
                    paperCollection.add(paper);
                    groupMap.put(group, paperCollection);
                }
            }
        }
        return groupMap;
    }

    /**
     * Sums the {@code Collection<Paper>} in {@code map} into an integer value depending on {@code sumBy}.
     * E.g. If {@code sumBy == PAPER}, the value will be the sum of papers.
     * If {@code sumBy == AUTHOR}, the value will be the sum of unique authors.
     */
    public static <T> Map<T, Map<Integer, Integer>> sumMaps(Map<T, Map<Integer, Collection<Paper>>> map, Measure measure) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // map to itself i.e. no change
                entry -> sumMap(entry.getValue(), measure))); // map to sumMap function
    }

    /**
     * Sums the {@code Collection<Paper>} in {@code map} into an integer value depending on {@code measure}.
     * E.g. If {@code measure == PAPER}, the value will be the sum of papers.
     * If {@code measure == AUTHOR}, the value will be the sum of unique authors.
     */
    public static <T> Map<T, Integer> sumMap(Map<T, Collection<Paper>> map, Measure measure) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // map to itself i.e. no change
                getSumFunction(measure), // map to getSumFunction
                (value1, value2) -> value1, // resolve duplicate keys (will not happen, but need it to map it to TreeMap)
                TreeMap::new)); // ensures that Map is ordered by key

    }

    /**
     * Returns a {@code Function} that returns the value of a field in {@code Paper} depending on {@code groupBy}.
     */
    private static Collection<String> getGroupsFromPaper(Category groupBy, Paper paper) {
        Collection<String> groups = new ArrayList<>();
        switch (groupBy) {
            case PAPER :
                groups.add(paper.getTitle());
                break;
            case TOTAL :
                groups.add(TOTAL.toString().toLowerCase());
                break;
            case VENUE :
                groups.add(paper.getVenue());
                break;
            case AUTHOR :
                groups = paper.getAuthors().stream().map(Author::getName).collect(Collectors.toList());
                break;
            default :
                throw new AssertionError("Should not reach here; these are all the possible enums.");
        }
        return groups;
    }

    /**
     * Returns a {@code Function} that returns an integer value depending on {@code sumBy} and {@code Collection<Paper>}.
     * E.g. If {@code sumBy == PAPER}, the value will be the sum of papers.
     * If {@code sumBy == AUTHOR}, the value will be the sum of unique authors.
     */
    private static <T> Function<Map.Entry<T, Collection<Paper>>, Integer> getSumFunction(Measure sumBy) {
        switch (sumBy) {
            case AUTHOR:
                return entry -> (int) entry.getValue().stream()
                        .map(Paper::getAuthors)
                        .flatMap(Set::stream)
                        .distinct()
                        .count();
            case PAPER:
                return entry -> entry.getValue().size();
            case VENUE:
                return entry -> (int) entry.getValue().stream()
                        .map(Paper::getVenue)
                        .distinct()
                        .count();
            case INCITATION:
                return entry -> (int) entry.getValue().stream()
                        .map(Paper::getInCitation)
                        .flatMap(Collection::stream)
                        .distinct()
                        .count();
            case OUTCITATION:
                return entry -> (int) entry.getValue().stream()
                        .map(Paper::getOutCitation)
                        .flatMap(Collection::stream)
                        .distinct()
                        .count();
            default:
                throw new AssertionError("Should not reach here; these are all the possible enums.");
        }
    }

    /**
     * Merges keys in {@code map} that are considered as equal. E.g. {@code venue == "ICSE"} and {@code venue == "ICSE@ICSE"}
     * are considered as equal, however they are mapped into different keys.
     */
    public static Map<String, Collection<Paper>> mergeEqualKeys(Map<String, Collection<Paper>> map, Filter filter) {
        if (filter == null) {
            // We should merge all if possible, but currently, no implementation is faster than O(n^2)
            return map;
        } else {

            return mergeSelectedWords(map, filter);
        }
    }

    private static Map<String, Collection<Paper>> mergeSelectedWords(Map<String, Collection<Paper>> map, Filter filter) {
        Map<String, Collection<Paper>> mergedMap = new HashMap<>();
        for (String keyword1 : filter.getValuesToFilter()) {
            map.keySet().stream()
                    .filter(keyword2 -> isKeywordEqual(filter, keyword2, keyword1))
                    .forEach(key -> mergedMap.merge(keyword1, map.getOrDefault(key, Collections.emptyList()),
                            CollectionUtility.mergeLists()));
        }
        return mergedMap;
    }

    private static boolean isKeywordEqual(Filter filter, String str1, String str2) {
        return isKeywordEqual(filter.toQueryKeyword(), str1, str2);
    }

    private static boolean isKeywordEqual(Category category, String str1, String str2) {
        switch (category) {
            case PAPER :
            case AUTHOR :
                return str1.equalsIgnoreCase(str2);
            case VENUE :
                return StringUtil.containsMatchIgnoreCaseAndPunctuation(str1, str2);
            case TOTAL :
                return true;
            default :
                throw new AssertionError("Should not reach here; these are all the possible enums.");
        }
    }
}

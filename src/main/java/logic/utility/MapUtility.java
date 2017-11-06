package logic.utility;

import static logic.model.QueryKeyword.TOTAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logic.model.Filter;
import logic.model.PaperTitleFilter;
import logic.model.PaperVenueFilter;
import logic.model.QueryKeyword;
import model.Paper;
import util.StringUtil;

/**
 * Utility class providing methods to map {@code Collection} and remap {@code Map}.
 */
public class MapUtility {
    /**
     * Groups the {@code Collection<Paper>} in {@code map} according to years.
     */
    public static <T> Map<T, Map<Integer, Collection<Paper>>> groupPaper(Map<T, Collection<Paper>> map) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // map to itself i.e. no change
                entry -> entry.getValue().stream().collect(Collectors.groupingBy(Paper::getYear, // group by years
                        Collectors.toCollection(ArrayList::new)))));
    }

    /**
     * Groups {@code papers} according to {@code groupBy}.
     * E.g. If {@code groupBy == VENUE}, groups papers according to their venue names.
     */
    public static Map<String, Collection<Paper>> groupPaper(Collection<Paper> papers, QueryKeyword groupBy) {
        return papers.stream()
                .collect(Collectors.groupingBy(getMappingFunction(groupBy), Collectors.toCollection(ArrayList::new)));
    }

    /**
     * Sums the {@code Collection<Paper>} in {@code map} into an integer value depending on {@code sumBy}.
     * E.g. If {@code sumBy == PAPER}, the value will be the sum of papers.
     * If {@code sumBy == AUTHOR}, the value will be the sum of unique authors.
     */
    public static <T> Map<T, Map<Integer, Integer>> sumMaps(Map<T, Map<Integer, Collection<Paper>>> map, QueryKeyword sumBy) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // map to itself i.e. no change
                entry -> sumMap(entry.getValue(), sumBy))); // map to sumMaps function
    }

    /**
     * Sums the {@code Collection<Paper>} in {@code map} into an integer value depending on {@code sumBy}.
     * E.g. If {@code sumBy == PAPER}, the value will be the sum of papers.
     * If {@code sumBy == AUTHOR}, the value will be the sum of unique authors.
     */
    public static <T> Map<T, Integer> sumMap(Map<T, Collection<Paper>> map, QueryKeyword sumBy) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // map to itself i.e. no change
                getSumFunction(sumBy))); // map to sumMaps function
    }

    /**
     * Returns a {@code Function} that returns the value of a field in {@code Paper} depending on {@code groupBy}.
     */
    private static Function<Paper, String> getMappingFunction(QueryKeyword groupBy) {
        switch (groupBy) {
            case TITLE:
                return Paper::getTitle;
            case TOTAL:
                return paper -> TOTAL.toString().toLowerCase();
            case VENUE:
                return Paper::getVenue;
            default:
                throw new AssertionError("Not yet implemented.");
        }
    }

    /**
     * Returns a {@code Function} that returns an integer value depending on {@code sumBy} and {@code Collection<Paper>}.
     * E.g. If {@code sumBy == PAPER}, the value will be the sum of papers.
     * If {@code sumBy == AUTHOR}, the value will be the sum of unique authors.
     */
    private static <T> Function<Map.Entry<T, Collection<Paper>>, Integer> getSumFunction(QueryKeyword sumBy) {
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
                throw new AssertionError("Not yet implemented.");
        }
    }

    /**
     * Merges keys in {@code map} that are considered as equal. E.g. {@code venue == "ICSE"} and {@code venue == "ICSE@ICSE"}
     * are considered as equal, however they are mapped into different keys.
     */
    public static Map<String, Collection<Paper>> mergeEqualKeys(Map<String, Collection<Paper>> map, Filter filter,
            QueryKeyword category) {
        if (filter == null) {
            return mergeAllWords(map, category);
        } else {
            return mergeSelectedWords(map, filter);
        }
    }

    private static Map<String, Collection<Paper>> mergeAllWords(Map<String, Collection<Paper>> map, QueryKeyword category) {
        Set<String> usedKeys = new HashSet<>();
        Map<String, Collection<Paper>> mergedMap = new HashMap<>();
        for (String key1 : map.keySet()) {
            for (String key2 : map.keySet()) {
                if (usedKeys.contains(key1) || key1.equals(key2)) {
                    continue;
                }

                Predicate<String> predicate = getPredicate(category, key2);
                if (predicate.test(key1)) {
                    Collection<Paper> mergedPapers = CollectionUtility.mergeLists().apply(map.get(key1), map.get(key2));
                    mergedMap.put(key1, mergedPapers);
                    usedKeys.addAll(Arrays.asList(key1, key2));
                }
            }
            if (!mergedMap.containsKey(key1) && !usedKeys.contains(key1)) {
                mergedMap.put(key1, map.get(key1));
            }
        }
        return mergedMap;
    }

    private static Map<String, Collection<Paper>> mergeSelectedWords(Map<String, Collection<Paper>> map, Filter filter) {
        Map<String, Collection<Paper>> mergedMap = new HashMap<>();
        for (String keyword : getKeywords(filter)) {
            map.keySet().stream()
                    .filter(getPredicate(filter, keyword))
                    .forEach(key -> mergedMap.merge(keyword, map.getOrDefault(key, Collections.emptyList()),
                            CollectionUtility.mergeLists()));
        }
        return mergedMap;
    }

    private static Predicate<String> getPredicate(Filter filter, String searchKeyword) {
        return getPredicate(filter.toQueryKeyword(), searchKeyword);
    }

    private static Predicate<String> getPredicate(QueryKeyword category, String searchKeyword) {
        switch (category) {
            case TITLE:
                return key -> StringUtil.containsIgnoreCase(key, searchKeyword);
            case VENUE:
                return key -> StringUtil.containsIgnoreCaseVenue(key, searchKeyword);
            default:
                throw new AssertionError("Not yet implemented");
        }
    }

    private static Collection<String> getKeywords(Filter filter) {
        if (filter instanceof PaperTitleFilter) {
            return filter.getValuesToFilter();
        } else if (filter instanceof PaperVenueFilter) {
            return filter.getValuesToFilter();
        } else {
            throw new AssertionError("Not yet implemented");
        }
    }
}

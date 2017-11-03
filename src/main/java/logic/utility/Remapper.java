package logic.utility;

import static logic.model.MapEnum.SELF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import logic.model.MapEnum;
import logic.model.QueryKeyword;
import model.Paper;

public class Remapper {
    /**
     * Remaps {@code Collection<Paper>} in {@code papers} by grouping them with {@code groupBy}.
     * E.g. If {@code groupBy == PAPER_YEAR}, the papers will be grouped according years.
     */
    public static <T> Map<T, Map<Integer, Collection<Paper>>> groupPaper(Map<T, Collection<Paper>> papers, MapEnum groupBy) {
        return papers.entrySet().stream().collect(Collectors.toMap(getKeyFunction(SELF), getValueFunction(groupBy)));
    }

    /**
     * Remaps {@code Collection<Paper>} in {@code map} into an integer value depending on {@code ordering}.
     * E.g. If {@code ordering == PAPER}, the value will be the sum of papers. If {@code ordering == AUTHOR}, the value
     * will be the sum of unique authors.
     */
    public static <T> Map<T, Map<Integer, Integer>> sum(Map<T, Map<Integer, Collection<Paper>>> map,
            QueryKeyword ordering) {
        return map.entrySet().stream().collect(Collectors.toMap(getKeyFunction(SELF), entry ->
                entry.getValue().entrySet().stream()
                        .collect(Collectors.toMap(getKeyFunction(SELF), getSumFunction(ordering)))));
    }

    /**
     * Returns a {@code Function} depending on {@code key}.
     */
    private static <T> Function<Map.Entry<T, ?>, T> getKeyFunction(MapEnum key) {
        switch (key) {
            case SELF:
                return Map.Entry::getKey;
            default:
                throw new AssertionError("Not yet implemented.");
        }
    }

    /**
     * Returns a {@code Function} that groups the {@code Collection<Paper>} by {@code groupBy}.
     */
    private static <T> Function<Map.Entry<T, Collection<Paper>>, Map<Integer, Collection<Paper>>> getValueFunction(
            MapEnum groupBy) {
        switch (groupBy) {
            case PAPER_YEAR:
                return entry -> entry.getValue().stream()
                        .collect(Collectors.groupingBy(Paper::getYear, Collectors.toCollection(ArrayList::new)));
            default:
                throw new AssertionError("Not yet implemented.");
        }
    }

    /**
     * Returns the function that remaps the {@code Collection<Paper>} into an integer value depending on {@code ordering}.
     * E.g. If {@code ordering == PAPER}, the value will be the sum of papers. If {@code ordering == AUTHOR}, the value
     * will be the sum of unique authors.
     */
    private static <T> Function<Map.Entry<T, Collection<Paper>>, Integer> getSumFunction(QueryKeyword ordering) {
        switch (ordering) {
            case AUTHOR:
                return entry1 -> (int) entry1.getValue().stream()
                        .map(Paper::getAuthors)
                        .flatMap(Set::stream)
                        .distinct()
                        .count();
            case PAPER:
                return entry2 -> entry2.getValue().size();
            default:
                throw new AssertionError("Not yet implemented.");
        }
    }
}

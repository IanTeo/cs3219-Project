package logic.model;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Paper;

/**
 * Tests that a {@code Paper}'s {@code year} falls within the range of {@code startYear} and {@code endYear}.
 */
public class YearPredicate implements Predicate<Paper> {
    private final YearRange yearRange;

    public YearPredicate(YearRange yearRange) {
        this.yearRange = yearRange;
    }

    public Collection<String> getSearchYears() {
        return yearRange.stream().boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public boolean test(Paper paper) {
        return paper.getYear() >= yearRange.getStartYear() && paper.getYear() <= yearRange.getEndYear();
    }
}

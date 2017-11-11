package logic.filter;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logic.model.Category;
import logic.model.YearRange;
import model.Paper;

public class YearFilter extends Filter {
    public YearFilter(YearRange yearRange) {
        this.predicate = new YearPredicate(yearRange);
    }

    @Override
    public Category toQueryKeyword() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((YearPredicate) predicate).getSearchYears();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof YearFilter
                && this.getValuesToFilter().equals(((YearFilter) other).getValuesToFilter());
    }

    @Override
    public int hashCode() {
        return this.getValuesToFilter().hashCode();
    }

    private static class YearPredicate implements Predicate<Paper> {
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
}

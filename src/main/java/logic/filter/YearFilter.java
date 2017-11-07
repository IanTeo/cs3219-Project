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

    private class YearPredicate implements Predicate<Paper> {
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

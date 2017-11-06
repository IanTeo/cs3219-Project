package logic.model;

import java.util.Collection;

public class YearFilter extends Filter {
    public YearFilter(YearRange yearRange) {
        this.predicate = new YearPredicate(yearRange);
    }

    @Override
    public QueryKeyword toQueryKeyword() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((YearPredicate) predicate).getSearchYears();
    }
}

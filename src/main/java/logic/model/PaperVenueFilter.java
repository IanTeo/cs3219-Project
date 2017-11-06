package logic.model;

import static logic.model.QueryKeyword.VENUE;

import java.util.Collection;

public class PaperVenueFilter extends Filter {
    public PaperVenueFilter(Collection<String> keywords) {
        this.predicate = new PaperVenuePredicate(keywords);
    }

    @Override
    public QueryKeyword toQueryKeyword() {
        return VENUE;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((PaperVenuePredicate) predicate).getSearchVenues();
    }
}

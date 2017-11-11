package logic.filter;

import static logic.model.Category.VENUE;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import logic.model.Category;
import model.Paper;
import util.StringUtil;

public class PaperVenueFilter extends Filter {
    public PaperVenueFilter(String... keywords) {
        this.predicate = new PaperVenuePredicate(Arrays.asList(keywords));
    }

    @Override
    public Category toQueryKeyword() {
        return VENUE;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((PaperVenuePredicate) predicate).getSearchVenues();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PaperVenueFilter
                && this.getValuesToFilter().equals(((PaperVenueFilter) other).getValuesToFilter());
    }

    @Override
    public int hashCode() {
        return this.getValuesToFilter().hashCode();
    }

    private static class PaperVenuePredicate implements Predicate<Paper> {
        private final Collection<String> searchVenues;

        public PaperVenuePredicate(Collection<String> searchVenues) {
            this.searchVenues = searchVenues;
        }

        public Collection<String> getSearchVenues() {
            return searchVenues;
        }

        @Override
        public boolean test(Paper paper) {
            return searchVenues.stream().anyMatch(searchVenue -> StringUtil.containsIgnoreCase(paper.getVenue(), searchVenue));
        }
    }
}

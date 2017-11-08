package logic.filter;

import static logic.model.Category.VENUE;

import java.util.Collection;
import java.util.function.Predicate;

import logic.model.Category;
import model.Paper;
import util.StringUtil;

public class PaperVenueFilter extends Filter {
    public PaperVenueFilter(Collection<String> keywords) {
        this.predicate = new PaperVenuePredicate(keywords);
    }

    @Override
    public Category toQueryKeyword() {
        return VENUE;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((PaperVenuePredicate) predicate).getSearchVenues();
    }

    private class PaperVenuePredicate implements Predicate<Paper> {
        private final Collection<String> searchVenues;

        public PaperVenuePredicate(Collection<String> searchVenues) {
            this.searchVenues = searchVenues;
        }

        public Collection<String> getSearchVenues() {
            return searchVenues;
        }

        @Override
        public boolean test(Paper paper) {
            return searchVenues.stream().anyMatch(searchVenue -> StringUtil.containsIgnoreCaseVenue(paper.getVenue(), searchVenue));
        }
    }
}

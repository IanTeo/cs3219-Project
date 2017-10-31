package logic.model;

import java.util.Collection;
import java.util.function.Predicate;

import util.StringUtil;

public class PaperVenuePredicate implements Predicate<String> {
    private final Collection<String> searchVenues;

    public PaperVenuePredicate(Collection<String> searchVenues) {
        this.searchVenues = searchVenues;
    }

    @Override
    public boolean test(String venue) {
        return searchVenues.stream().anyMatch(searchVenue -> StringUtil.containsIgnoreCaseVenue(venue, searchVenue));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PaperVenuePredicate)) {
            return false;
        }

        PaperVenuePredicate otherPredicate = (PaperVenuePredicate) other;
        return this.searchVenues.equals(otherPredicate.searchVenues);
    }
}

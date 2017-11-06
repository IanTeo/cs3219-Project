package logic.model;

import java.util.Collection;
import java.util.function.Predicate;

import model.Paper;
import util.StringUtil;

/**
 * Tests that a {@code Paper}'s venue matches any of the keywords given.
 */
public class PaperVenuePredicate implements Predicate<Paper> {
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

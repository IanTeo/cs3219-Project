package logic.model;

import java.util.Collection;
import java.util.function.Predicate;

import model.Paper;
import util.StringUtil;

/**
 * Tests that a {@code Paper}'s title matches any of the keywords given.
 */
public class PaperTitlePredicate implements Predicate<Paper> {
    private final Collection<String> searchTitles;

    public PaperTitlePredicate(Collection<String> searchTitles) {
        this.searchTitles = searchTitles;
    }

    public Collection<String> getSearchTitles() {
        return searchTitles;
    }

    @Override
    public boolean test(Paper paper) {
        return searchTitles.stream().anyMatch(searchTitle -> StringUtil.containsIgnoreCase(paper.getTitle(), searchTitle));
    }
}

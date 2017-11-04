package logic.model;

import java.util.Collection;
import java.util.function.Predicate;

import util.StringUtil;

public class PaperTitlePredicate implements Predicate<String> {
    private final Collection<String> searchTitles;

    public PaperTitlePredicate(Collection<String> searchTitles) {
        this.searchTitles = searchTitles;
    }

    @Override
    public boolean test(String title) {
        return searchTitles.stream().anyMatch(searchTitle -> StringUtil.containsIgnoreCase(title, searchTitle));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PaperTitlePredicate)) {
            return false;
        }

        PaperTitlePredicate otherPredicate = (PaperTitlePredicate) other;
        return this.searchTitles.equals(otherPredicate.searchTitles);
    }
}

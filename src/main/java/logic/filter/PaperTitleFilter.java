package logic.filter;

import static logic.model.Category.PAPER;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import logic.model.Category;
import model.Paper;

public class PaperTitleFilter extends Filter {
    public PaperTitleFilter(String... keywords) {
        this.predicate = new PaperTitlePredicate(Arrays.asList(keywords));
    }

    @Override
    public Category toQueryKeyword() {
        return PAPER;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((PaperTitlePredicate) predicate).getSearchTitles();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PaperTitleFilter
                && this.getValuesToFilter().equals(((PaperTitleFilter) other).getValuesToFilter());
    }

    @Override
    public int hashCode() {
        return this.getValuesToFilter().hashCode();
    }

    private static class PaperTitlePredicate implements Predicate<Paper> {
        private final Collection<String> searchTitles;

        public PaperTitlePredicate(Collection<String> searchTitles) {
            this.searchTitles = searchTitles;
        }

        public Collection<String> getSearchTitles() {
            return searchTitles;
        }

        @Override
        public boolean test(Paper paper) {
            return searchTitles.stream().anyMatch(searchTitle -> paper.getTitle().equalsIgnoreCase(searchTitle));
        }
    }
}

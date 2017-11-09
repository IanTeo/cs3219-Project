package logic.filter;

import static logic.model.Category.PAPER;

import java.util.Collection;
import java.util.function.Predicate;

import logic.model.Category;
import model.Paper;

public class PaperTitleFilter extends Filter {
    public PaperTitleFilter(Collection<String> keywords) {
        this.predicate = new PaperTitlePredicate(keywords);
    }

    @Override
    public Category toQueryKeyword() {
        return PAPER;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((PaperTitlePredicate) predicate).getSearchTitles();
    }

    private class PaperTitlePredicate implements Predicate<Paper> {
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

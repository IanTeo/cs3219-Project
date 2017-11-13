package logic.filter;

import static logic.model.Category.PAPER;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logic.model.Category;
import model.Paper;
import util.StringUtil;

public class PaperTitleFilter extends Filter {
    public PaperTitleFilter(String... keywords) {

        List<String> keywordList = Arrays.asList(keywords).stream()
                .map(StringUtil::sanitise)
                .collect(Collectors.toList());
        this.predicate = new PaperTitlePredicate(keywordList);
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

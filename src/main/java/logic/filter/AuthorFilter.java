package logic.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logic.model.Category;
import model.Author;
import model.Paper;

import static logic.model.Category.AUTHOR;

public class AuthorFilter extends Filter {
    public AuthorFilter(String... keywords) {
        this.predicate = new AuthorPredicate(Arrays.asList(keywords));
    }

    @Override
    public Category toQueryKeyword() {
        return AUTHOR;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((AuthorPredicate) predicate).getSearchNames();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AuthorFilter
                && this.getValuesToFilter().equals(((AuthorFilter) other).getValuesToFilter());
    }

    @Override
    public int hashCode() {
        return this.getValuesToFilter().hashCode();
    }

    private static class AuthorPredicate implements Predicate<Paper> {
        private final Collection<String> searchNames;

        public AuthorPredicate(Collection<String> searchNames) {
            this.searchNames = searchNames;
        }

        public Collection<String> getSearchNames() {
            return searchNames;
        }

        @Override
        public boolean test(Paper paper) {
            return searchNames.stream().anyMatch(searchName -> {
                Collection<String> authorNames = paper.getAuthors().stream().map(Author::getName).collect(Collectors.toList());
                return authorNames.stream().anyMatch(authorName -> authorName.equalsIgnoreCase(searchName));
            });
        }
    }
}

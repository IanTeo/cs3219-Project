package logic.filter;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logic.model.Category;
import model.Author;
import model.Paper;

public class AuthorFilter extends Filter {
    public AuthorFilter(Collection<String> keywords) {
        this.predicate = new AuthorPredicate(keywords);
    }

    @Override
    public Category toQueryKeyword() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((AuthorPredicate) predicate).getSearchNames();
    }

    private class AuthorPredicate implements Predicate<Paper> {
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

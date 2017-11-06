package logic.model;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Author;
import model.Paper;
import util.StringUtil;

public class AuthorPredicate implements Predicate<Paper> {
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
            return authorNames.stream().anyMatch(authorName -> StringUtil.containsIgnoreCase(authorName, searchName));
        });
    }
}

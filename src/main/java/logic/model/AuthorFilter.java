package logic.model;

import java.util.Collection;

public class AuthorFilter extends Filter {
    public AuthorFilter(Collection<String> keywords) {
        this.predicate = new AuthorPredicate(keywords);
    }

    @Override
    public QueryKeyword toQueryKeyword() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((AuthorPredicate) predicate).getSearchNames();
    }
}

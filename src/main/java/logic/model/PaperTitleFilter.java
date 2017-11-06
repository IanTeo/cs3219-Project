package logic.model;

import static logic.model.QueryKeyword.TITLE;

import java.util.Collection;

public class PaperTitleFilter extends Filter {
    public PaperTitleFilter(Collection<String> keywords) {
        this.predicate = new PaperTitlePredicate(keywords);
    }

    @Override
    public QueryKeyword toQueryKeyword() {
        return TITLE;
    }

    @Override
    public Collection<String> getValuesToFilter() {
        return ((PaperTitlePredicate) predicate).getSearchTitles();
    }
}

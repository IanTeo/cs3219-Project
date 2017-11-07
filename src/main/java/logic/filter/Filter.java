package logic.filter;

import java.util.Collection;
import java.util.function.Predicate;

import logic.model.Category;
import logic.utility.CollectionUtility;
import model.Paper;

public abstract class Filter {
    protected Predicate<Paper> predicate;

    public abstract Collection<String> getValuesToFilter();

    public abstract Category toQueryKeyword();

    public void filter(Collection<Paper> papers) {
        CollectionUtility.removeFromCollection(papers, predicate);
    }
}
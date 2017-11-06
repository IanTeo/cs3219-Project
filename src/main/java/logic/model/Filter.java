package logic.model;

import java.util.Collection;
import java.util.function.Predicate;

import logic.utility.CollectionUtility;
import model.Paper;

public abstract class Filter {
    protected Predicate<Paper> predicate;

    public abstract Collection<String> getValuesToFilter();

    public abstract QueryKeyword toQueryKeyword();

    public void filter(Collection<Paper> papers) {
        CollectionUtility.removeFromCollection(papers, predicate);
    }
}
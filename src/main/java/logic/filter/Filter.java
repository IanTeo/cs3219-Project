package logic.filter;

import java.util.Collection;
import java.util.function.Predicate;

import logic.model.Category;
import logic.utility.CollectionUtility;
import model.Paper;

public abstract class Filter {
    protected Predicate<Paper> predicate;

    public static Filter getFilterOfCategory(Category category, Collection<Filter> filters) {

        switch (category) {
            case PAPER :
                return filters.stream()
                        .filter(filter -> filter instanceof PaperTitleFilter)
                        .findFirst()
                        .orElse(null);
            case VENUE :
                return filters.stream()
                        .filter(filter -> filter instanceof PaperVenueFilter)
                        .findFirst()
                        .orElse(null);

            case AUTHOR :
                return filters.stream()
                        .filter(filter -> filter instanceof AuthorFilter)
                        .findFirst()
                        .orElse(null);

            case TOTAL :
            default :
                return null;
        }
    }

    public abstract Collection<String> getValuesToFilter();

    public abstract Category toQueryKeyword();

    public Collection<Paper> filter(Collection<Paper> papers) {
        return CollectionUtility.removeFromCollection(papers, predicate);
    }
}
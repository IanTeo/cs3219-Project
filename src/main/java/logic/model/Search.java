package logic.model;

import java.util.List;

/**
 * Represents the category and the keywords of a search.
 */
public class Search {
    private final QueryKeyword searchCategory;
    private final List<String> searchKeywords;

    public Search(QueryKeyword searchCategory, List<String> searchKeywords) {
        this.searchCategory = searchCategory;
        this.searchKeywords = searchKeywords;
    }

    public QueryKeyword getSearchCategory() {
        return searchCategory;
    }

    public List<String> getSearchKeywords() {
        return searchKeywords;
    }
}

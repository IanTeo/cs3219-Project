package logic.command;

import logic.model.QueryKeyword;
import model.Model;

import java.util.Map;

public interface Command {
    public String execute();

    public void setParameters(Model model, Map<String, String> argumentMap) throws Exception;

    /**
     * Returns true if {@code key} is a currently supported search term.
     */
    static boolean isValidSearchCategory(QueryKeyword key) {
        switch (key) {
            case AUTHOR:
            case VENUE:
            case TITLE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns true if {@code key} is a currently supported ordering for the search results.
     */
     static boolean isValidOrdering(QueryKeyword keyword) {
        switch (keyword) {
            case PAPER:
            case AUTHOR:
                return true;
            default:
                return false;
        }
    }
}
